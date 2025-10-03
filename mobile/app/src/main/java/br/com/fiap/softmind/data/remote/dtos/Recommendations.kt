package br.com.fiap.softmind.data.remote.dtos

data class Recommendations(
    val supports: List<Any>, // vazio no momento, pode ser ajustado se tiver estrutura depois
    val movies: List<MovieDto>,
    val physicalActivities: List<PhysicalActDto>
)
