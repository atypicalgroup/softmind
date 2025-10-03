package br.com.atypical.Softmind.Report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@Schema(description = "Relatório consolidado semanal de pesquisas de bem-estar")
public class AdminReportDTO {

    @Schema(
            description = "Lista de resumos das pesquisas realizadas no período",
            implementation = SurveySummaryDTO.class
    )
    private List<SurveySummaryDTO> surveySummary;

    @Schema(
            description = "Resumo agregado da semana (maior engajamento, sentimento mais votado, etc.)",
            implementation = AdminReportWeekSummaryDTO.class
    )
    private AdminReportWeekSummaryDTO weekSummary;

    @Schema(
            description = "Variação percentual do índice de bem-estar em relação à semana anterior",
            example = "12.50"
    )
    private BigDecimal healthyPercentage;

    @Schema(
            description = "Data de início da semana analisada",
            example = "2025-09-20"
    )
    private LocalDate startOfWeek;

    @Schema(
            description = "Data de término da semana analisada",
            example = "2025-10-03"
    )
    private LocalDate endOfWeek;
}
