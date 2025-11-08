package br.com.fiap.softmind.data.remote.model

data class ValidateCodeRequest(
    val email: String,
    val token: String
)
