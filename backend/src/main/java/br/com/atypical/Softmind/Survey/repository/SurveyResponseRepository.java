package br.com.atypical.Softmind.Survey.repository;

import br.com.atypical.Softmind.Survey.entities.SurveyResponse;
import br.com.atypical.Softmind.Survey.service.SurveyResponseService;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SurveyResponseRepository extends MongoRepository<SurveyResponse, String> {
    Optional<SurveyResponse> findTopBySurveyIdAndEmployeeIdOrderByAnsweredAtDesc(
            String employeeId, String surveyId
    );
}
