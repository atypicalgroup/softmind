package br.com.atypical.Softmind.Report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDTO {
    private String question;
    private List<DailyResponseDTO> dailyResponses;
}
