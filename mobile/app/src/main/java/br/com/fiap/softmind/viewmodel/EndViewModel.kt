package br.com.fiap.softmind.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softmind.data.remote.ApiClient
import br.com.fiap.softmind.data.remote.dtos.MovieDto
import br.com.fiap.softmind.data.remote.model.MoodRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EndViewModel : ViewModel() {

    private val _movies = MutableStateFlow<List<MovieDto>>(emptyList())
    val movies: StateFlow<List<MovieDto>> = _movies

    fun loadRecommendations(emoji: String, feeling: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.moodService.getDailyRecommendation(
                    MoodRequest(emoji, feeling)
                )
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        _movies.value = body.recommendations.movies
                    }
                } else {
                    Log.e("MOOD", "Erro: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("MOOD", "Falha na conexão", e)
            }
        }
    }
}