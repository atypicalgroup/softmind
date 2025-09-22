package br.com.atypical.Softmind.Report.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class SurveySummaryDTO {
    private final String surveyId;
    private final String surveyTitle;
    private final List<QuestionResponseDTO> questionResponses;
    private final SurveyParticipantsDTO participants;
    private final BigDecimal engagement;
}
