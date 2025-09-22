package br.com.atypical.Softmind.Report.dto;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

@Getter
@Builder
public class ResponseDTO implements Comparable<ResponseDTO> {

    private final String response;
    private final Long quantity;

    @Override
    public int compareTo(@NotNull ResponseDTO o) {
        return Comparator.comparing(ResponseDTO::getQuantity)
                .reversed()
                .thenComparing(ResponseDTO::getResponse)
                .compare(this, o);
    }
}
