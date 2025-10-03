package br.com.fiap.softmind.data.remote.model

import br.com.fiap.softmind.data.remote.dtos.Recommendations
import br.com.fiap.softmind.data.remote.dtos.SuggestionDto

data class MoodResponse(
    val emoji: String,
    val feeling: String,
    val recommendations: Recommendations
)
