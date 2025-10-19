package br.com.atypical.Softmind.Report.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Builder
public class MoodSummaryDTO {

    private Map.Entry<String, BigDecimal> mainMoodOfWeek;
    private Map<String, BigDecimal> moods;
}
