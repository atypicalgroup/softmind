package br.com.atypical.Softmind.Report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Builder
@Schema(description = "Resumo consolidado da semana de pesquisas de bem-estar")
public class AdminReportWeekSummaryDTO {

    @Schema(
            description = "Mapa com as respostas mais votadas por pergunta da semana",
            example = "{ \"Qual o seu Emoji do dia?\": { \"Feliz\": 12 }, \"Como está sua motivação?\": { \"Alta\": 8 } }"
    )
    private final Map<String, Map<String, Long>> mostVotedResponses;

    @Schema(
            description = "Percentual médio de engajamento dos colaboradores na semana",
            example = "78.50"
    )
    private final BigDecimal overallEngagement;

    @Schema(
            description = "Informações sobre os participantes da semana (total e por setor)",
            implementation = SurveyParticipantsDTO.class
    )
    private final SurveyParticipantsDTO participants;
}
