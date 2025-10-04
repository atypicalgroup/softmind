package br.com.fiap.softmind.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softmind.data.remote.ApiClient
import br.com.fiap.softmind.data.remote.dtos.MovieDto
import br.com.fiap.softmind.data.remote.dtos.PhysicalActDto
import br.com.fiap.softmind.data.remote.model.MoodRequest
import br.com.fiap.softmind.data.remote.model.MoodResponse
import br.com.fiap.softmind.data.remote.model.PhysicalActivity
import br.com.fiap.softmind.data.utils.MoodStatusManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class MoodViewModel(private val statusManager: MoodStatusManager) : ViewModel() {
    private val _moodResponse = MutableStateFlow<MoodResponse?>(null)
    val moodResponse: StateFlow<MoodResponse?> = _moodResponse
    private val _activities = MutableStateFlow<List<PhysicalActDto>>(emptyList())
    val activities: StateFlow<List<PhysicalActDto>> = _activities

    private val _movies = MutableStateFlow<List<MovieDto>>(emptyList())
    val movies: StateFlow<List<MovieDto>> = _movies

    fun hasSubmittedToday(): Boolean {
        return statusManager.alreadyAnsweredToday()
    }

    fun loadRecommendations(emoji: String, feeling: String) {
        viewModelScope.launch {
            val response = ApiClient.moodService.getDailyRecommendation(MoodRequest(emoji, feeling))

            if (response.isSuccessful) {
                response.body()?.let { body ->
                    _movies.value = body.recommendations.movies
                    _activities.value = body.recommendations.physicalActivities
                    _moodResponse.value = body
                    statusManager.saveSubmissionDate()
                }
            }
        }
    }

    fun getNextDestinationAfterLogin(): String {
        return if (hasSubmittedToday()) {
            "EndScreen"
        } else {
            "EmojiScreen"
        }
    }

    fun clearPreviousRecommendation(){
        _movies.value = emptyList()
        _activities.value = emptyList()
    }
}