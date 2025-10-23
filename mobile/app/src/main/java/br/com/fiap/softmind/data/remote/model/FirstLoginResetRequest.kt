package br.com.fiap.softmind.data.remote.model

data class FirstLoginResetRequest(
    val userId: String,
    val newPassword: String
)
