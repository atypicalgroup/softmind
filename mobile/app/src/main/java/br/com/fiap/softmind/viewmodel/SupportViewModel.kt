package br.com.fiap.softmind.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softmind.data.remote.ApiClient
import br.com.fiap.softmind.data.remote.model.SupportPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SupportViewModel : ViewModel() {

    private val _supportPoints = MutableStateFlow<List<SupportPoint>>(emptyList())
    val supportPoints: StateFlow<List<SupportPoint>> = _supportPoints

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadSupportPoints() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = ApiClient.supportService.getSupportPoints()
                if (response.isSuccessful) {
                    _supportPoints.value = response.body().orEmpty()
                } else {
                    println("Erro ao carregar pontos de apoio: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Falha ao conectar: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
