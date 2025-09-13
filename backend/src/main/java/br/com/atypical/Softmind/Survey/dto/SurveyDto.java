package br.com.atypical.Softmind.Survey.dto;

import br.com.atypical.Softmind.Survey.entities.Question;

import java.time.LocalDateTime;
import java.util.List;

public record SurveyDto(
        String id,
        String companyId,
        String title,
        String description,
        List<QuestionDto> questions,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean active
) {
}
