package br.com.atypical.Softmind.Mood.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta do humor diário do funcionário")
public record DailyMoodRequestDto(
        @Schema(description = "Emoji escolhido", example = "😀") String emoji,
        @Schema(description = "Sentimento do dia", example = "feliz") String feeling
) {}
