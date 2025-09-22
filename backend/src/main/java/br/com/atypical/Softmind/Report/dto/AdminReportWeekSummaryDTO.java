package br.com.atypical.Softmind.Report.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Builder
public class AdminReportWeekSummaryDTO {
    private final Map<String, String> mostVotedResponses;
    private final BigDecimal overallEngagement;
    private final SurveyParticipantsDTO participants;
}
