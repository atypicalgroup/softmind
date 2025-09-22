package br.com.atypical.Softmind.Report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyResponseDTO implements Comparable<DailyResponseDTO> {
    private String date;
    private List<ResponseDTO> ranking;

    @Override
    public int compareTo(@NotNull DailyResponseDTO o) {
        return this.date.compareTo(o.date);
    }
}
