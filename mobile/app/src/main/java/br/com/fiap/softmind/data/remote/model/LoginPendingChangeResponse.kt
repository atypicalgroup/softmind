package br.com.fiap.softmind.data.remote.model

data class LoginPendingChangeResponse(
    val userId: String,
    val username: String,
    val message: String
)
