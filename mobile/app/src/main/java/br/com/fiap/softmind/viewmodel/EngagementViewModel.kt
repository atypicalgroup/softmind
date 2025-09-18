package br.com.fiap.softmind.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softmind.data.model.Engagement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class EngagementViewModel : ViewModel() {
    internal val _engagementSelected = MutableStateFlow<Engagement?>(null)
    val engagementSelected: StateFlow<Engagement?> = _engagementSelected

    init {
        loadRandomEngagement()
    }

    fun loadRandomEngagement() {
//        viewModelScope.launch {
//            try {
//                val response = RetrofitClient.instance.getEngagement()
//                if (response.isSuccessful) {
//                    response.body()?.let { engagementResponse ->
//                        val list = engagementResponse.engajamentos
//                        if (list.isNotEmpty()) {
//                            _engagementSelected.value = list.random()
//                        }
//                    }
//                } else {
//                    println("Erro na resposta da API: ${response.code()} - ${response.message()}")
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
    }
}