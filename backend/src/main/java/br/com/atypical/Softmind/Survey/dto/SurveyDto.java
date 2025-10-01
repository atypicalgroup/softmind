package br.com.atypical.Softmind.Survey.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Representação da pesquisa de bem-estar")
public record SurveyDto(
        @Schema(description = "Identificador único da pesquisa", example = "64f2a8dcb15d4c7a9c123456")
        String id,

        @Schema(description = "Identificador da empresa dona da pesquisa", example = "68c8925f0af554ee81fa402d")
        String companyId,

        @Schema(description = "Título da pesquisa", example = "Pesquisa de Clima Organizacional")
        String title,

        @Schema(description = "Descrição da pesquisa", example = "Pesquisa sobre satisfação dos colaboradores")
        String description,

        @Schema(description = "Questões cadastradas na pesquisa")
        List<QuestionDto> questions,

        @Schema(description = "Data de criação da pesquisa", example = "2025-09-30T10:15:30")
        LocalDateTime createdAt,

        @Schema(description = "Data da última atualização da pesquisa", example = "2025-09-30T10:15:30")
        LocalDateTime updatedAt,

        @Schema(description = "Status da pesquisa (ativa ou inativa)", example = "false")
        boolean active
) {}
