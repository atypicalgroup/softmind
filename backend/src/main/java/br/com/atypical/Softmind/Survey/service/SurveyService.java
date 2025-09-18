package br.com.atypical.Softmind.Survey.service;

import br.com.atypical.Softmind.Survey.dto.SurveyCreateDto;
import br.com.atypical.Softmind.Survey.dto.SurveyDto;
import br.com.atypical.Softmind.Survey.entities.Question;
import br.com.atypical.Softmind.Survey.entities.Survey;
import br.com.atypical.Softmind.Survey.mapper.SurveyMapper;
import br.com.atypical.Softmind.Survey.repository.SurveyRepository;
import br.com.atypical.Softmind.shared.enums.QuestionType;
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

    public SurveyDto createSurvey(SurveyCreateDto dto) {
        Survey survey = mapper.toEntity(dto);

        Question emoji = new Question();
        emoji.setText("Qual o seu Emoji do dia?");
        emoji.setType(QuestionType.EMOJI);
        emoji.setOptions(new String[] {"😀","😐","😢","😡","😍","😴"});

        Question feeling = new Question();
        feeling.setText("Qual o seu sentimento de hoje?");
        emoji.setType(QuestionType.EMOJI);
        emoji.setOptions(new String[] {"😀","😐","😢","😡","😍","😴"});

        survey.getQuestions().add(0, feeling);
        survey.getQuestions().add(0, emoji);

        survey.setCreatedAt(LocalDateTime.now());
        survey.setUpdatedAt(LocalDateTime.now());
        survey.setActive(true);

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

    public SurveyDto getSurveyForEmployee(String surveyId) {
        Survey survey = repository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        List<Question> finalQuestions = new ArrayList<>();

        Question emoji = new Question();
        emoji.setText("Qual o seu Emoji do dia?");
        emoji.setType(QuestionType.EMOJI);
        emoji.setOptions(new String[]{"😀","😐","😢","😡","😍","😴"});
        finalQuestions.add(emoji);

        Question feeling = new Question();
        feeling.setText("Qual o seu sentimento de hoje?");
        feeling.setType(QuestionType.TEXT);
        finalQuestions.add(feeling);

        if (survey.getQuestions() != null && !survey.getQuestions().isEmpty()) {
            Collections.shuffle(survey.getQuestions());
            finalQuestions.addAll(
                    survey.getQuestions().stream()
                            .limit(8) // sempre pega 8 quantidade de perguntas aleatorias
                            .toList()
            );
        }

        survey.setQuestions(finalQuestions);

        return mapper.toDto(survey);
    }

}
