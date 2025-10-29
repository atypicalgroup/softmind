package br.com.fiap.softmind.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softmind.data.remote.ApiClient
import br.com.fiap.softmind.data.remote.dtos.MovieDto
import br.com.fiap.softmind.data.remote.dtos.PhysicalActDto
import br.com.fiap.softmind.data.remote.model.MoodRequest
import br.com.fiap.softmind.data.remote.model.MoodResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MoodViewModel : ViewModel() {

    private val _moodResponse = MutableStateFlow<MoodResponse?>(null)
    val moodResponse: StateFlow<MoodResponse?> = _moodResponse

    private val _activities = MutableStateFlow<List<PhysicalActDto>>(emptyList())
    val activities: StateFlow<List<PhysicalActDto>> = _activities

    private val _movies = MutableStateFlow<List<MovieDto>>(emptyList())
    val movies: StateFlow<List<MovieDto>> = _movies

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /**
     * Envia as emoções (emoji + sentimento) para o backend e carrega recomendações
     */
    fun loadRecommendations(emoji: String, feeling: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = ApiClient.moodService.getDailyRecommendation(MoodRequest(emoji, feeling))

                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        _movies.value = body.recommendations.movies
                        _activities.value = body.recommendations.physicalActivities
                        _moodResponse.value = body
                    }
                } else {
                    Log.e("MOOD", "Erro na recomendação: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("MOOD", "Falha ao carregar recomendações", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Limpa as recomendações atuais (quando o usuário sai ou faz novo envio)
     */
    fun clearPreviousRecommendation() {
        _movies.value = emptyList()
        _activities.value = emptyList()
        _moodResponse.value = null
    }
}
