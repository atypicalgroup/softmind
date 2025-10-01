package br.com.atypical.Softmind.Survey.service;

import br.com.atypical.Softmind.Employee.entities.Employee;
import br.com.atypical.Softmind.Employee.repository.EmployeeRepository;
import br.com.atypical.Softmind.Survey.dto.SurveyCreateDto;
import br.com.atypical.Softmind.Survey.dto.SurveyDto;
import br.com.atypical.Softmind.Survey.entities.Question;
import br.com.atypical.Softmind.Survey.entities.Survey;
import br.com.atypical.Softmind.Survey.mapper.SurveyMapper;
import br.com.atypical.Softmind.Survey.repository.SurveyRepository;
import br.com.atypical.Softmind.security.entities.User;
import br.com.atypical.Softmind.security.service.UserService;
import br.com.atypical.Softmind.shared.enums.QuestionType;
import br.com.atypical.Softmind.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository repository;
    private final SurveyMapper mapper;
    private final EmployeeRepository employeeRepository;

    public SurveyDto createSurvey(SurveyCreateDto dto, User user) {

        Employee creator = employeeRepository.findById(user.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        Survey survey = mapper.toEntity(dto);
        survey.setCompanyId(creator.getCompanyId());
        survey.setCreatedAt(LocalDateTime.now());
        survey.setUpdatedAt(LocalDateTime.now());
        survey.setActive(false);

        return mapper.toDto(repository.save(survey));
    }

    public List<SurveyDto> getByCompany(String companyId) {
        return repository.findByCompanyId(companyId)
                .stream().map(mapper::toDto).toList();
    }

    public SurveyDto getById(String id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Survey not found"));
    }

    public SurveyDto getActiveSurveyForEmployee(User user) {
        Employee employee = employeeRepository.findById(user.getEmployeeId())
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));

        Survey activeSurvey = repository.findByCompanyIdAndActiveTrue(employee.getCompanyId())
                .orElseThrow(() -> new NotFoundException("Nenhuma pesquisa ativa encontrada"));

        List<Question> finalQuestions = new ArrayList<>();
        if (activeSurvey.getQuestions() != null && !activeSurvey.getQuestions().isEmpty()) {
            Collections.shuffle(activeSurvey.getQuestions());
            finalQuestions.addAll(
                    activeSurvey.getQuestions().stream()
                            .limit(8)
                            .toList()
            );
        }

        activeSurvey.setQuestions(finalQuestions);

        return mapper.toDto(activeSurvey);
    }



    public SurveyDto activateSurvey(String surveyId) {
        Survey survey = repository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        survey.setActive(true);
        survey.setUpdatedAt(LocalDateTime.now());
        return mapper.toDto(repository.save(survey));
    }

}
