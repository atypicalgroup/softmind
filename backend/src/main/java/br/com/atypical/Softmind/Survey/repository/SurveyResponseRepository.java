package br.com.atypical.Softmind.Survey.repository;

import br.com.atypical.Softmind.Survey.entities.SurveyResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SurveyResponseRepository extends MongoRepository<SurveyResponse, String> {

}
