package br.com.atypical.Softmind.Survey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(
        name = "SurveyResponseCreateDto",
        description = "Representa a resposta enviada pelo colaborador para a pesquisa diária de bem-estar."
)
public record SurveyResponseCreateDto(

        @Schema(
                description = "Lista de respostas às perguntas da pesquisa.",
                required = true,
                example = """
                        [
                          { "questionText": "Como está seu humor hoje?", "response": "😊" },
                          { "questionText": "Com que frequência se sente motivado no trabalho?", "response": "Frequentemente" }
                        ]
                        """
        )
        List<AnswerDto> answers,

        @Schema(
                description = "Data e hora em que o colaborador respondeu à pesquisa. Caso omitida, será preenchida automaticamente pelo servidor.",
                example = "2025-10-26T08:30:00"
        )
        LocalDateTime participationDate
) {}
