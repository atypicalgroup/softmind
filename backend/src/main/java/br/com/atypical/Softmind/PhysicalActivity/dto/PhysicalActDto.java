package br.com.atypical.Softmind.PhysicalActivity.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de um vídeo de atividade física")
public record PhysicalActDto(

        @Schema(description = "ID do vídeo (proveniente da fonte, como YouTube)", example = "dQw4w9WgXcQ")
        String videoId,

        @Schema(description = "Título do vídeo", example = "Treino de Alongamento para Iniciantes")
        String title,

        @Schema(description = "Descrição do vídeo", example = "Vídeo com exercícios leves de alongamento para praticar em casa")
        String description,

        @Schema(description = "URL da thumbnail do vídeo", example = "https://img.youtube.com/vi/dQw4w9WgXcQ/maxresdefault.jpg")
        String thumbnailUrl,

        @Schema(description = "Link direto para o vídeo", example = "https://www.youtube.com/watch?v=dQw4w9WgXcQ")
        String videoLink
) {}
