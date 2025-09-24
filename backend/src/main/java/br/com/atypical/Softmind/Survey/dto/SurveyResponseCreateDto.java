package br.com.atypical.Softmind.Survey.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SurveyResponseCreateDto(
        String surveyId,
        String employeeId,
        List<AnswerDto> answers,
        LocalDateTime participationDate
) {}
