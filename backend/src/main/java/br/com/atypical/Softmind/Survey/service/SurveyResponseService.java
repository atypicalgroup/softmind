package br.com.atypical.Softmind.Survey.service;

import br.com.atypical.Softmind.Employee.entities.Employee;
import br.com.atypical.Softmind.Employee.repository.EmployeeRepository;
import br.com.atypical.Softmind.Survey.dto.SurveyResponseCreateDto;
import br.com.atypical.Softmind.Survey.entities.Answer;
import br.com.atypical.Softmind.Survey.entities.Survey;
import br.com.atypical.Softmind.Survey.entities.SurveyParticipation;
import br.com.atypical.Softmind.Survey.entities.SurveyResponse;
import br.com.atypical.Softmind.Survey.repository.SurveyParticipationRepository;
import br.com.atypical.Softmind.Survey.repository.SurveyRepository;
import br.com.atypical.Softmind.Survey.repository.SurveyResponseRepository;
import br.com.atypical.Softmind.Security.entities.User;
import br.com.atypical.Softmind.shared.exceptions.NotFoundException;
import br.com.atypical.Softmind.shared.utils.EmojiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SurveyResponseService {

    private final SurveyResponseRepository responseRepo;
    private final SurveyParticipationRepository participationRepo;
    private final SurveyRepository surveyRepository;
    private final EmployeeRepository employeeRepository;

    private static final ZoneId ZONE_BR = ZoneId.of("America/Sao_Paulo");

    @Transactional
    public SurveyResponse saveAnonymousDailyResponse(User user, SurveyResponseCreateDto dto) {
        Employee employee = employeeRepository.findById(user.getEmployeeId())
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));

        // Busca a survey ativa da empresa
        Survey activeSurvey = surveyRepository.findByCompanyIdAndActiveTrue(employee.getCompanyId())
                .orElseThrow(() -> new NotFoundException("Nenhuma pesquisa ativa encontrada"));

        LocalDateTime participationDate = Optional.ofNullable(dto.participationDate())
                .orElse(LocalDateTime.now(ZONE_BR));

        LocalDateTime startDate = participationDate.toLocalDate().atStartOfDay();
        LocalDateTime endDate = participationDate.toLocalDate().atTime(23, 59);

        // 1) Garante no máx. 1 resposta por dia
        participationRepo.findByEmployeeIdAndSurveyIdAndParticipationDateBetween(
                employee.getId(), activeSurvey.getId(), startDate, endDate
        ).ifPresent(p -> {
            throw new RuntimeException("Já respondeu esta pesquisa hoje.");
        });

        // 2) Salva resposta anônima
        SurveyResponse resp = new SurveyResponse();
        resp.setSurveyId(activeSurvey.getId());
        resp.setAnsweredAt(participationDate);
        resp.setAnswers(dto.answers().stream().map(a -> {
            Answer ans = new Answer();
            ans.setQuestionText(a.questionText());
            ans.setResponse(EmojiUtils.mapEmojiToDescription(a.response()));
            return ans;
        }).toList());
        SurveyResponse saved = responseRepo.save(resp);

        // 3) Registra participação
        SurveyParticipation part = new SurveyParticipation();
        part.setEmployeeId(employee.getId());
        part.setSurveyId(activeSurvey.getId());
        part.setParticipationDate(participationDate);
        part.setCreatedAt(LocalDateTime.now(ZONE_BR));
        part.setUniqueKey(employee.getId() + "|" + activeSurvey.getId() + "|" + participationDate.toLocalDate());
        participationRepo.save(part);

        return saved;
    }


    public long countEmployeeResponses(User user) {
        Employee employee = employeeRepository.findById(user.getEmployeeId())
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));

        Survey activeSurvey = surveyRepository.findByCompanyIdAndActiveTrue(employee.getCompanyId())
                .orElseThrow(() -> new NotFoundException("Nenhuma pesquisa ativa encontrada"));

        return participationRepo.countByEmployeeIdAndSurveyId(employee.getId(), activeSurvey.getId());
    }

    public String getEmojiResponse(User user) {
        Employee employee = employeeRepository.findById(user.getEmployeeId())
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));

        Survey activeSurvey = surveyRepository.findByCompanyIdAndActiveTrue(employee.getCompanyId())
                .orElseThrow(() -> new NotFoundException("Nenhuma pesquisa ativa encontrada"));

        Optional<SurveyResponse> optionalResponse = responseRepo
                .findTopBySurveyIdAndEmployeeIdOrderByAnsweredAtDesc(activeSurvey.getId(), employee.getId());

        SurveyResponse response = optionalResponse
                .orElseThrow(() -> new RuntimeException("Nenhuma resposta encontrada"));

        if (response.getAnswers().isEmpty()) {
            throw new RuntimeException("Nenhuma resposta registrada");
        }

        return response.getAnswers().get(0).getResponse();
    }
}
