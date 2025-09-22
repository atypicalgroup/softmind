package br.com.atypical.Softmind.Report.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class SurveyParticipantsDTO {

    private final Map<String, Long> totalPerSector;
    private final long total;
}
