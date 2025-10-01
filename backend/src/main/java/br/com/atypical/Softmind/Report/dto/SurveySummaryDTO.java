package br.com.atypical.Softmind.Report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@Schema(description = "Resumo de uma pesquisa de bem-estar aplicada na empresa")
public class SurveySummaryDTO {

    @Schema(description = "Identificador único da pesquisa", example = "64f2a8dcb15d4c7a9c123456")
    private final String surveyId;

    @Schema(description = "Título da pesquisa", example = "Pesquisa de Clima Organizacional - Setembro/2025")
    private final String surveyTitle;

    @Schema(description = "Respostas agregadas por pergunta da pesquisa")
    private final List<QuestionResponseDTO> questionResponses;

    @Schema(description = "Dados de participação dos colaboradores na pesquisa")
    private final SurveyParticipantsDTO participants;

    @Schema(description = "Percentual de engajamento dos colaboradores na pesquisa", example = "82.75")
    private final BigDecimal engagement;
}
