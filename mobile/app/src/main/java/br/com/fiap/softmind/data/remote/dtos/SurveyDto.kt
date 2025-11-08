package br.com.fiap.softmind.data.remote.dtos

data class SurveyDto(
    val id: String,
    val companyId: String,
    val title: String,
    val description: String,
    val questions: List<SurveyQuestion>,
    val createdAt: String,
    val updatedAt: String,
    val active: Boolean,
    val alreadyAnswered: Boolean
)

