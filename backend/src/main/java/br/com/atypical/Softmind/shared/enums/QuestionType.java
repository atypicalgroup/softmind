package br.com.atypical.Softmind.shared.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipos de perguntas permitidos na pesquisa de bem-estar.")
public enum QuestionType {

    @Schema(description = "Pergunta aberta, onde o colaborador digita uma resposta livre.")
    TEXT,

    @Schema(description = "Pergunta com uma única opção de resposta.")
    SINGLE_CHOICE,

    @Schema(description = "Pergunta com múltiplas opções de resposta possíveis.")
    MULTIPLE_CHOICE,

    @Schema(description = "Pergunta baseada em emojis (para capturar estado emocional).")
    EMOJI,

    @Schema(description = "Pergunta baseada em escala")
    SCALE
}
