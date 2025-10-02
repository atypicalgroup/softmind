package br.com.atypical.Softmind.Mood.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta do humor diário")
public record DailyMoodResponseDto(
        @Schema(description = "ID do funcionário", example = "68c9e931d06dc42160de80ff")
        String employeeId,

        @Schema(description = "Emoji escolhido", example = "😀")
        String emoji,

        @Schema(description = "Sentimento textual", example = "Feliz e animado")
        String feeling
){}
