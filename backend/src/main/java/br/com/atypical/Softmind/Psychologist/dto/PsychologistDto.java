package br.com.atypical.Softmind.Psychologist.dto;

public record PsychologistDto(
        Integer id,
        String name,
        String description,
        String[] contactNumber
) {
}
