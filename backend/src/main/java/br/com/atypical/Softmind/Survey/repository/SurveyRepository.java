package br.com.atypical.Softmind.Survey.repository;

import br.com.atypical.Softmind.Survey.entities.Survey;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SurveyRepository extends MongoRepository<Survey, String> {
    List<Survey> findByCompanyId(String companyId);
}
