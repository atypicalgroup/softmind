package br.com.atypical.Softmind.Psychologist.mapper;

import br.com.atypical.Softmind.Psychologist.dto.PsychologistDto;
import br.com.atypical.Softmind.Psychologist.entities.Psychologist;

public class PsychologistMapper {

    private PsychologistDto toDto(Psychologist psychologist) {
        return new PsychologistDto(
                psychologist.getId(),
                psychologist.getName(),
                psychologist.getDescription(),
                psychologist.getContactNumber()
        );
    };
}
