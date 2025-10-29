package br.com.fiap.softmind.data.remote.model

data class LoginResponse(
    val token: String,
    val username: String,
    val name: String,
    val alreadyAnswered: Boolean
)
