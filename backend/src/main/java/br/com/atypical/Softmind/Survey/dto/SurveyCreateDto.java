package br.com.atypical.Softmind.Survey.dto;

import br.com.atypical.Softmind.Survey.entities.Question;

import java.util.List;

public record SurveyCreateDto(
        String companyId,
        String title,
        String description,
        List<QuestionDto> questions
) {
}
