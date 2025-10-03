package br.com.fiap.softmind.data.remote.model


data class EmployeeDailyResponseRequest(
    val answers: List<SurveyAnswer>,
    val participationDate: String
)