package br.com.atypical.Softmind.Psychologist.dto;

public record PsychologistDto(
        String id,
        String name,
        String description,
        String[] contactNumber
) {
}
