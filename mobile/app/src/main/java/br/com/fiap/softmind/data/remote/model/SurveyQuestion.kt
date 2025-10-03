package br.com.fiap.softmind.data.remote.model

data class SurveyQuestion(
    val id: String,
    val questionText: String,
    val options: List<String>
)
