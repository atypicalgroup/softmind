package br.com.atypical.Softmind.Psychologist.repository;

import br.com.atypical.Softmind.Psychologist.entities.Psychologist;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PsychologistRepository extends MongoRepository<Psychologist, String> {
    List<Psychologist> findByName(String name);
}
