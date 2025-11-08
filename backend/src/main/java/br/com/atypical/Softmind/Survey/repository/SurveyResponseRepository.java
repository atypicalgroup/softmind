package br.com.atypical.Softmind.Survey.repository;

import br.com.atypical.Softmind.Survey.entities.SurveyResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SurveyResponseRepository extends MongoRepository<SurveyResponse, String> {

    List<SurveyResponse> findBySurveyIdAndAnsweredAtBetween(String surveyId, LocalDateTime answeredAtAfter, LocalDateTime answeredAtBefore);

    Optional<SurveyResponse> findTopBySurveyIdOrderByAnsweredAtDesc(String id);

    List<SurveyResponse> findBySurveyId(String surveyId);

}

