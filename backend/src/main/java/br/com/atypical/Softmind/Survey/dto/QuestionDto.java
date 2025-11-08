package br.com.atypical.Softmind.Survey.dto;

import br.com.atypical.Softmind.shared.enums.QuestionType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "QuestionDto",
        description = "Representa uma pergunta da pesquisa de bem-estar, com seu texto, tipo e possíveis opções de resposta."
)
public record QuestionDto(

        @Schema(
                description = "Texto da pergunta exibida ao colaborador.",
                example = "Com que frequência você se sente motivado no trabalho?"
        )
        String text,

        @Schema(
                description = "Tipo da pergunta (ex: TEXT, SINGLE_CHOICE, MULTIPLE_CHOICE, EMOJI).",
                example = "SINGLE_CHOICE"
        )
        QuestionType type,

        @Schema(
                description = "Opções de resposta disponíveis (aplicável apenas para perguntas de escolha única ou múltipla).",
                example = "[\"Nunca\", \"Raramente\", \"Às vezes\", \"Frequentemente\", \"Sempre\"]"
        )
        String[] options
) {}
