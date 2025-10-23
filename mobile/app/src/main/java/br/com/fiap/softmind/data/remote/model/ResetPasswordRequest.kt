package br.com.fiap.softmind.data.remote.model

data class ResetPasswordRequest(
    val email: String,
    val token: String,
    val newPassword: String
)
