package br.com.atypical.Softmind.Survey.mapper;

import br.com.atypical.Softmind.Survey.dto.QuestionDto;
import br.com.atypical.Softmind.Survey.dto.SurveyCreateDto;
import br.com.atypical.Softmind.Survey.dto.SurveyDto;
import br.com.atypical.Softmind.Survey.entities.Question;
import br.com.atypical.Softmind.Survey.entities.Survey;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SurveyMapper {

    public Survey toEntity(SurveyCreateDto dto) {
        Survey survey = new Survey();
        survey.setTitle(dto.title());
        survey.setDescription(dto.description());
        survey.setQuestions(
                dto.questions() == null ? new ArrayList<>() :
                        dto.questions().stream()
                                .map(q -> {
                                    var question = new Question();
                                    question.setText(q.text());
                                    question.setType(q.type());
                                    question.setOptions(q.options());
                                    return question;
                                })
                                .collect(Collectors.toCollection(ArrayList::new))
        );
        return survey;
    }

    public SurveyDto toDto(Survey entity, boolean alreadyAnswered) {
        return new SurveyDto(
                entity.getId(),
                entity.getCompanyId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getQuestions() == null ? List.of() :
                        entity.getQuestions().stream()
                                .map(this::toQuestionDto)
                                .toList(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.isActive()
        );
    }

    public QuestionDto toQuestionDto(Question question) {
        return new QuestionDto(question.getText(), question.getType(), question.getOptions());
    }
}
