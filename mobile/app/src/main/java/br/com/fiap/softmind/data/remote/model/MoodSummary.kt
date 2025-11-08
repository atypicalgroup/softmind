package br.com.fiap.softmind.data.remote.model

import com.google.gson.annotations.SerializedName

data class MoodSummary(
    @SerializedName("mostCommonMood")
    val mostCommonMood: String? = null,

    @SerializedName("averageMoodScore")
    val averageMoodScore: Double? = null
)
