package br.com.atypical.Softmind.Report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@Schema(description = "Informações sobre os participantes da pesquisa")
public class SurveyParticipantsDTO {

    @Schema(
            description = "Distribuição de participantes por setor da empresa",
            example = "{\"Comercial\": 15, \"RH\": 8, \"TI\": 20}"
    )
    private final Map<String, Long> totalPerSector;

    @Schema(
            description = "Total de colaboradores que participaram da pesquisa",
            example = "43"
    )
    private final long total;
}
