package br.com.atypical.Softmind.Psychologist.service;

import br.com.atypical.Softmind.Psychologist.dto.PsychologistDto;
import br.com.atypical.Softmind.Psychologist.entities.Psychologist;
import br.com.atypical.Softmind.Psychologist.exception.RecursoNaoEncontrado;
import br.com.atypical.Softmind.Psychologist.repository.PsychologistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PsychologistService {

    private final PsychologistRepository psychologistRepository;

    public PsychologistDto savePsychologist(PsychologistDto psychologistDto) {
        Psychologist psychologist = new Psychologist();
        psychologist.setId(psychologistDto.id());
        psychologist.setName(psychologistDto.name());
        psychologist.setDescription(psychologistDto.description());
        psychologist.setContactNumber(psychologistDto.contactNumber());

        Psychologist psychologistSalvo = psychologistRepository.save(psychologist);

        return new PsychologistDto(psychologistSalvo.getId(), psychologistSalvo.getName(), psychologistSalvo.getDescription(), psychologistSalvo.getContactNumber());
    }

    public PsychologistDto updatePsychologist(String name, PsychologistDto psychologistDto) {
        Psychologist psychologist = findPsychologistByName(name);

        if(psychologistDto.id() != null) psychologist.setId(psychologistDto.id());
        if(psychologistDto.name() != null) psychologist.setName(psychologistDto.name());
        if(psychologistDto.description() != null) psychologist.setDescription(psychologistDto.description());
        if(psychologistDto.contactNumber() != null) psychologist.setContactNumber(psychologistDto.contactNumber());

        Psychologist psychologistSalvo = psychologistRepository.save(psychologist);

        return new PsychologistDto(psychologistSalvo.getId(), psychologistSalvo.getName(), psychologistSalvo.getDescription(), psychologistSalvo.getContactNumber());

    }

    public Boolean deletePsychologist(String name) {
        if(psychologistRepository.existsById(name)){
            psychologistRepository.deleteById(name);
            return true;
        }
        return false;
    }

    private Psychologist findPsychologistByName(String name) {
        return psychologistRepository.findByName(name)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RecursoNaoEncontrado("Nenhum psicólogo encontrado"));
    }
}
