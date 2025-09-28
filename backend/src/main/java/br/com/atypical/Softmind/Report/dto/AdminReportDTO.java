package br.com.atypical.Softmind.Report.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class AdminReportDTO {

    private List<SurveySummaryDTO> surveySummary;
    private AdminReportWeekSummaryDTO weekSummary;
    private BigDecimal healthyPercentage;
    private LocalDate startOfWeek;
    private LocalDate endOfWeek;
}
