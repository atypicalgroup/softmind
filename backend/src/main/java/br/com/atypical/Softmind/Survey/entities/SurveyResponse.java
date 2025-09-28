package br.com.atypical.Softmind.Survey.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "gs_tb_survey_responses")
public class SurveyResponse {
    @Id
    private String id;
    private String surveyId;
    private String employeeId;
    private List<Answer> answers;
    private LocalDateTime answeredAt;
}
