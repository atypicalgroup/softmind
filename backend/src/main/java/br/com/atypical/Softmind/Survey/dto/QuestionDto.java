package br.com.atypical.Softmind.Survey.dto;

import br.com.atypical.Softmind.shared.enums.QuestionType;

public record QuestionDto(
        String text,
        QuestionType type,
        String[] options
) {
}
