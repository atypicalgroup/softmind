package br.com.fiap.softmind.data.model

import br.com.fiap.softmind.data.remote.model.MoodSummary
import br.com.fiap.softmind.data.remote.model.WeekSummary
import com.google.gson.annotations.SerializedName

data class AdminReport(
    @SerializedName("weekSummary")
    val weekSummary: WeekSummary? = null,

    @SerializedName("previousHealthyPercentage")
    val previousHealthyPercentage: Double? = null,

    @SerializedName("currentHealthyPercentage")
    val currentHealthyPercentage: Double? = null,

    @SerializedName("healthyPercentage")
    val healthyPercentage: Double? = null,

    @SerializedName("startOfWeek")
    val startOfWeek: String? = null,

    @SerializedName("endOfWeek")
    val endOfWeek: String? = null,

    @SerializedName("alerts")
    val alerts: List<String>? = null,

    @SerializedName("moodSummary")
    val moodSummary: MoodSummary? = null
)





