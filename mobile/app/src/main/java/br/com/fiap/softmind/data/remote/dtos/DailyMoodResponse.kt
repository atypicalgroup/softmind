package br.com.fiap.softmind.data.remote.dtos

data class DailyMoodResponse(
    val emoji: String,
    val recommendations: Recommendations,
    val feeling: String
)