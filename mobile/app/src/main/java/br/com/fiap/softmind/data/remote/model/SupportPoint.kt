package br.com.fiap.softmind.data.remote.model

data class SupportPoint(
    val id: String,
    val name: String,
    val description: String,
    val contactNumber: List<String>

)
