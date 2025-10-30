package br.com.fiap.softmind.data.remote.model

import com.google.gson.annotations.SerializedName

data class WeekSummary(
    @SerializedName("mostVotedResponses")
    val mostVotedResponses: Map<String, Map<String, Long>>? = null,

    @SerializedName("overallEngagement")
    val overallEngagement: Double? = null,

    @SerializedName("participants")
    val participants: Participants? = null
)
