package br.com.fiap.softmind.data.remote.dtos

data class SupportDto( // Crie o DTO para o SupportService.listAll()
    // Exemplo:
    val id: String,
    val name: String,
    val description: String,
    val contactNumber: String
)