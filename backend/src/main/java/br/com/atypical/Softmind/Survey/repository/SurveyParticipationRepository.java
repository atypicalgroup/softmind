package br.com.atypical.Softmind.Survey.repository;

import br.com.atypical.Softmind.Survey.entities.SurveyParticipation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface SurveyParticipationRepository extends MongoRepository<SurveyParticipation, String> {
    Optional<SurveyParticipation> findByEmployeeIdAndSurveyIdAndParticipationDate(
            String employeeId, String surveyId, LocalDate participationDate);
    long countByEmployeeIdAndSurveyId(String employeeId, String surveyId);
}

