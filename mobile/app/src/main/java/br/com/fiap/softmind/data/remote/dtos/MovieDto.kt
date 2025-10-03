package br.com.fiap.softmind.data.remote.dtos

data class MovieDto(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val posterUrl: String
)