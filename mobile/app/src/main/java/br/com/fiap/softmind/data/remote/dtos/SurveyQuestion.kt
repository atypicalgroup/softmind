package br.com.fiap.softmind.data.remote.dtos

data class SurveyQuestion(
    val text: String,
    val type: String, // SCALE ou MULTIPLE_CHOICE
    val options: List<String>
)
