package br.com.fiap.softmind.data.model

data class EngagementResponse(
    val engajamentos: List<Engagement>
)

data class Engagement(
    val engajamento_colaboradores: Double,
    val sentimento_destaque: String,
    val bem_estar_emocional: Double,
    val variacao_percentual: Double,
    val tendencia: String,
    val emoji_representativo: String,
    val comentario: String,
    val periodo: String
)
