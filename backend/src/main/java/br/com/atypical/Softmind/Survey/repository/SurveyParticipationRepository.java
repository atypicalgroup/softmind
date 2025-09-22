package br.com.atypical.Softmind.Survey.repository;

import br.com.atypical.Softmind.Survey.entities.SurveyParticipation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SurveyParticipationRepository extends MongoRepository<SurveyParticipation, String> {
    Optional<SurveyParticipation> findByEmployeeIdAndSurveyIdAndParticipationDateBetween(
            String employeeId, String surveyId, LocalDateTime participationDateAtAfter, LocalDateTime participationDateAtBefore);
    long countByEmployeeIdAndSurveyId(String employeeId, String surveyId);
    List<SurveyParticipation> findBySurveyIdAndParticipationDateBetween(String surveyId, LocalDateTime answeredAtAfter, LocalDateTime answeredAtBefore);
}

