package br.com.fiap.softmind.data.remote.dtos

// 🟢 NOVO DTO DE AGRUPAMENTO (SuggestionDto)
data class SuggestionDto(
    // Espelha o Map.of da SuggestionService
    val movies: List<MovieDto>,
    val supports: List<SupportDto>, // Ajuste o tipo se o seu SupportService retornar algo diferente
    val physicalActivities: List<PhysicalActDto>
)