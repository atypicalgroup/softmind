package br.com.atypical.Softmind.Survey.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Dados para criação de uma nova pesquisa")
public record SurveyCreateDto(
        @Schema(description = "Título da pesquisa", example = "Pesquisa de Clima Organizacional")
        String title,

        @Schema(description = "Descrição da pesquisa", example = "Pesquisa sobre satisfação dos colaboradores")
        String description,

        @Schema(description = "Questões da pesquisa")
        List<QuestionDto> questions
) {}
