package br.com.fiap.softmind.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.softmind.data.remote.ApiClient
import br.com.fiap.softmind.data.remote.model.MoodRequest
import br.com.fiap.softmind.data.remote.model.MoodResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MoodViewModel : ViewModel() {

    private val _moodResponse = MutableStateFlow<MoodResponse?>(null)
    val moodResponse: StateFlow<MoodResponse?> = _moodResponse

    fun loadRecommendations(emoji: String, feeling: String) {
        viewModelScope.launch {
            val response = ApiClient.moodService.getDailyRecommendation(
                MoodRequest(emoji, feeling)
            )
            if (response.isSuccessful) {
                _moodResponse.value = response.body()
            }
        }
    }
}