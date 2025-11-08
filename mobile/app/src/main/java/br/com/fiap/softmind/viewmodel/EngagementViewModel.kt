package br.com.fiap.softmind.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softmind.data.model.AdminReport
import br.com.fiap.softmind.data.remote.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EngagementViewModel : ViewModel() {

    private val _adminReport = MutableStateFlow<AdminReport?>(null)
    val adminReport: StateFlow<AdminReport?> = _adminReport

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadAdminReport() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null

                val response = ApiClient.reportService.getAdminReport()
                if (response.isSuccessful) {
                    _adminReport.value = response.body()
                } else {
                    _errorMessage.value = "Erro ao carregar relatório (${response.code()})"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Erro desconhecido"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
