package br.com.atypical.Softmind.Survey.service;

import br.com.atypical.Softmind.Survey.dto.SurveyResponseCreateDto;
import br.com.atypical.Softmind.Survey.entities.Answer;
import br.com.atypical.Softmind.Survey.entities.SurveyParticipation;
import br.com.atypical.Softmind.Survey.entities.SurveyResponse;
import br.com.atypical.Softmind.Survey.repository.SurveyParticipationRepository;
import br.com.atypical.Softmind.Survey.repository.SurveyResponseRepository;
import br.com.atypical.Softmind.shared.utils.EmojiUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class SurveyResponseService {

    private final SurveyResponseRepository responseRepo;
    private final SurveyParticipationRepository participationRepo;
    private static final ZoneId ZONE_BR = ZoneId.of("America/Sao_Paulo");
    private final SurveyService surveyService;

    public SurveyResponseService(SurveyResponseRepository r, SurveyParticipationRepository p, SurveyService surveyService) {
        this.responseRepo = r;
        this.participationRepo = p;
        this.surveyService = surveyService;
    }

    @Transactional
    public SurveyResponse saveAnonymousDailyResponse(SurveyResponseCreateDto dto) {
        LocalDateTime participationDate = Optional.ofNullable(dto.participationDate()).orElse(LocalDateTime.now(ZONE_BR));
        LocalDateTime startDate = participationDate.toLocalDate().atStartOfDay();
        LocalDateTime endDate = participationDate.toLocalDate().atTime(23, 59);

        // 1) Garante no máx. 1 resposta por dia
        participationRepo.findByEmployeeIdAndSurveyIdAndParticipationDateBetween(
                dto.employeeId(), dto.surveyId(), startDate, endDate
        ).ifPresent(p -> {
            throw new RuntimeException("Já respondeu esta pesquisa hoje.");
        });

        // 2) Salva resposta anônima
        SurveyResponse resp = new SurveyResponse();
        resp.setSurveyId(dto.surveyId());
        resp.setAnsweredAt(participationDate);
        resp.setAnswers(dto.answers().stream().map(a -> {
            Answer ans = new Answer();
            ans.setQuestionText(a.questionText());
            ans.setResponse(EmojiUtils.mapEmojiToDescription(a.response()));
            ans.setResponse(a.response()); // emoji
            ans.setResponse(EmojiUtils.mapEmojiToDescription(a.response()));  //talvez necessario criar uma setdescription para as entities
            return ans;
        }).toList());
        SurveyResponse saved = responseRepo.save(resp);

        // 3) Registra participação
        SurveyParticipation part = new SurveyParticipation();
        part.setEmployeeId(dto.employeeId());
        part.setSurveyId(dto.surveyId());
        part.setParticipationDate(participationDate);
        part.setCreatedAt(LocalDateTime.now(ZONE_BR));
        part.setUniqueKey(dto.employeeId() + "|" + dto.surveyId() + "|" + participationDate.toLocalDate());
        participationRepo.save(part);

        return saved;
    }

    public long countEmployeeResponses(String employeeId, String surveyId) {
        return participationRepo.countByEmployeeIdAndSurveyId(employeeId, surveyId);
    }

    public String getEmojiResponse(String surveyId, String employeeId) {
        Optional<SurveyResponse> optionalResponse = responseRepo
                .findTopBySurveyIdAndEmployeeIdOrderByAnsweredAtDesc(surveyId, employeeId);

        SurveyResponse response = optionalResponse
                .orElseThrow(() -> new RuntimeException("Nenhuma resposta encontrada"));

        if (response.getAnswers().isEmpty()) {
            throw new RuntimeException("Nenhuma resposta registrada");
        }

        // já devolve só o emoji
        return response.getAnswers().get(0).getResponse();
    }

}




