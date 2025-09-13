package br.com.atypical.Softmind.Survey.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "gs_tb_participations")
public class SurveyParticipation {
    @Id
    private String id;
    @Indexed
    private String employeeId;
    @Indexed
    private String surveyId;
    @Indexed
    private LocalDate participationDate;
    private LocalDateTime createdAt;
    @Indexed(unique = true)
    private String uniqueKey; // employeeId + "|" + surveyId + "|" + participationDate.toString()
}

