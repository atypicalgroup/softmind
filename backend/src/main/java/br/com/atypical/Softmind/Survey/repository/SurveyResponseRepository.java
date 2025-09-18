package br.com.atypical.Softmind.Survey.repository;

import br.com.atypical.Softmind.Survey.entities.SurveyResponse;
import br.com.atypical.Softmind.Survey.service.SurveyResponseService;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SurveyResponseRepository extends MongoRepository<SurveyResponse, String> {

}
