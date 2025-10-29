package br.com.atypical.Softmind.Survey.service;

import br.com.atypical.Softmind.Employee.entities.Employee;
import br.com.atypical.Softmind.Employee.repository.EmployeeRepository;
import br.com.atypical.Softmind.Survey.dto.SurveyCreateDto;
import br.com.atypical.Softmind.Survey.dto.SurveyDto;
import br.com.atypical.Softmind.Survey.entities.Question;
import br.com.atypical.Softmind.Survey.entities.Survey;
import br.com.atypical.Softmind.Survey.mapper.SurveyMapper;
import br.com.atypical.Softmind.Survey.repository.SurveyParticipationRepository;
import br.com.atypical.Softmind.Survey.repository.SurveyRepository;
import br.com.atypical.Softmind.Security.entities.User;
import br.com.atypical.Softmind.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final EmployeeRepository employeeRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyParticipationRepository participationRepository;
    private final SurveyMapper mapper;

    private static final ZoneId ZONE_BR = ZoneId.of("America/Sao_Paulo");

    public SurveyDto createSurvey(SurveyCreateDto dto, User user) {
        Employee creator = employeeRepository.findById(user.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        Survey survey = mapper.toEntity(dto);
        survey.setCompanyId(creator.getCompanyId());
        survey.setCreatedAt(LocalDateTime.now());
        survey.setUpdatedAt(LocalDateTime.now());
        survey.setActive(false);

        return mapper.toDto(surveyRepository.save(survey), false);
    }

    public List<SurveyDto> getByCompany(String companyId) {
        return surveyRepository.findByCompanyId(companyId)
                .stream()
                .map(s -> mapper.toDto(s, false))
                .toList();
    }

    public SurveyDto getById(String id) {
        return surveyRepository.findById(id)
                .map(s -> mapper.toDto(s, false))
                .orElseThrow(() -> new RuntimeException("Survey not found"));
    }

    public SurveyDto getActiveSurveyForEmployee(User user) {
        Employee employee = employeeRepository.findById(user.getEmployeeId())
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));

        Survey activeSurvey = surveyRepository.findByCompanyIdAndActiveTrue(employee.getCompanyId())
                .orElseThrow(() -> new NotFoundException("Nenhuma pesquisa ativa encontrada"));

        // 🔹 verifica se já respondeu hoje
        LocalDateTime startOfDay = LocalDate.now(ZONE_BR).atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now(ZONE_BR).atTime(23, 59);
        boolean alreadyAnswered = participationRepository
                .findByEmployeeIdAndSurveyIdAndParticipationDateBetween(
                        employee.getId(),
                        activeSurvey.getId(),
                        startOfDay,
                        endOfDay
                )
                .isPresent();

        List<Question> finalQuestions = new ArrayList<>();
        if (activeSurvey.getQuestions() != null && !activeSurvey.getQuestions().isEmpty()) {
            Collections.shuffle(activeSurvey.getQuestions());
            finalQuestions.addAll(activeSurvey.getQuestions().stream().limit(8).toList());
        }
        activeSurvey.setQuestions(finalQuestions);

        return mapper.toDto(activeSurvey, alreadyAnswered);
    }

    public SurveyDto activateSurvey(String surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        survey.setActive(true);
        survey.setUpdatedAt(LocalDateTime.now());

        return mapper.toDto(surveyRepository.save(survey), false);
    }
}
