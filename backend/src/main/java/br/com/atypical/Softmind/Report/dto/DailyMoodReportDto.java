package br.com.atypical.Softmind.Report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Schema(description = "Relatório diário de humor e engajamento dos colaboradores")
public record DailyMoodReportDto(

        @Schema(description = "Data de início do período analisado", example = "2025-09-22")
        LocalDate startDate,

        @Schema(description = "Data de fim do período analisado", example = "2025-09-28")
        LocalDate endDate,

        @Schema(description = "Percentual de engajamento dos colaboradores no período", example = "75.50")
        BigDecimal engagement,

        @Schema(description = "Pontuação média de humor dos colaboradores", example = "12.30")
        BigDecimal averageScore,

        @Schema(description = "Distribuição de respostas de humor (ex.: Feliz, Triste, Neutro)",
                example = "{\"Feliz\": 15, \"Triste\": 3, \"Neutro\": 7}")
        Map<String, Long> feelingDistribution
) {}
