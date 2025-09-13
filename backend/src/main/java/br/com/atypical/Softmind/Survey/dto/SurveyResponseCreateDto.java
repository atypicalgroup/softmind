package br.com.atypical.Softmind.Survey.dto;

import java.util.List;

public record SurveyResponseCreateDto(
        String surveyId,
        String employeeId,
        List<AnswerDto> answers
) {}
