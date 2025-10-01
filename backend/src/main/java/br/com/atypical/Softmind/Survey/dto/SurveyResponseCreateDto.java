package br.com.atypical.Softmind.Survey.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SurveyResponseCreateDto(
        List<AnswerDto> answers,
        LocalDateTime participationDate
) {}
