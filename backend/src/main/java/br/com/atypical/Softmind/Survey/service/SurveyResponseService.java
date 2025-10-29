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

import java.time.LocalDate;
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

        Survey activeSurvey = surveyRepository.findByCompanyIdAndActiveTrue(employee.getCompanyId())
                .orElseThrow(() -> new NotFoundException("Nenhuma pesquisa ativa encontrada"));

        LocalDateTime participationDate = Optional.ofNullable(dto.participationDate())
                .orElse(LocalDateTime.now(ZONE_BR));

        // 🔹 1) Intercepta e registra a participação ANTES da resposta
        registerParticipation(employee, activeSurvey, participationDate);

        // 🔹 2) Salva a resposta de forma 100% anônima
        SurveyResponse anonymousResponse = new SurveyResponse();
        anonymousResponse.setSurveyId(activeSurvey.getId());
        anonymousResponse.setAnsweredAt(participationDate);
        anonymousResponse.setAnswers(dto.answers().stream().map(a -> {
            Answer ans = new Answer();
            ans.setQuestionText(a.questionText());
            ans.setResponse(EmojiUtils.mapEmojiToDescription(a.response()));
            return ans;
        }).toList());

        return responseRepo.save(anonymousResponse);

    }


    private void registerParticipation(Employee employee, Survey survey, LocalDateTime participationDate) {
        LocalDateTime startOfDay = participationDate.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = participationDate.toLocalDate().atTime(23, 59);

        boolean alreadyParticipated = participationRepo
                .findByEmployeeIdAndSurveyIdAndParticipationDateBetween(
                        employee.getId(), survey.getId(), startOfDay, endOfDay
                )
                .isPresent();

        if (alreadyParticipated) {
            throw new RuntimeException("Já respondeu esta pesquisa hoje.");
        }

        SurveyParticipation part = new SurveyParticipation();
        part.setEmployeeId(employee.getId());
        part.setSurveyId(survey.getId());
        part.setParticipationDate(participationDate);
        part.setCreatedAt(LocalDateTime.now(ZONE_BR));
        part.setUniqueKey(employee.getId() + "|" + survey.getId() + "|" + participationDate.toLocalDate());
        participationRepo.save(part);

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

        // 🔹 1) Verifica se o funcionário respondeu hoje
        LocalDate today = LocalDate.now(ZONE_BR);
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        boolean hasParticipated = participationRepo
                .findByEmployeeIdAndSurveyIdAndParticipationDateBetween(
                        employee.getId(),
                        activeSurvey.getId(),
                        startOfDay,
                        endOfDay
                )
                .isPresent();

        if (!hasParticipated) {
            throw new RuntimeException("O funcionário ainda não respondeu hoje.");
        }

        // 🔹 2) Busca a resposta mais recente do survey (não vinculada a funcionário)
        Optional<SurveyResponse> optionalResponse = responseRepo
                .findTopBySurveyIdOrderByAnsweredAtDesc(activeSurvey.getId());

        SurveyResponse response = optionalResponse
                .orElseThrow(() -> new RuntimeException("Nenhuma resposta encontrada para a pesquisa."));

        if (response.getAnswers().isEmpty()) {
            throw new RuntimeException("Nenhuma resposta registrada.");
        }

        // 🔹 3) Retorna o primeiro emoji (ou qualquer outro critério que desejar)
        return response.getAnswers().get(0).getResponse();
    }


    public boolean hasAnsweredToday(User user) {
        Employee employee = employeeRepository.findById(user.getEmployeeId())
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));

        Survey activeSurvey = surveyRepository.findByCompanyIdAndActiveTrue(employee.getCompanyId())
                .orElseThrow(() -> new NotFoundException("Nenhuma pesquisa ativa encontrada"));

        LocalDate today = LocalDate.now(ZoneId.of("America/Sao_Paulo"));
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        return participationRepo.findByEmployeeIdAndSurveyIdAndParticipationDateBetween(
                employee.getId(),
                activeSurvey.getId(),
                startOfDay,
                endOfDay
        ).isPresent();
    }

}
