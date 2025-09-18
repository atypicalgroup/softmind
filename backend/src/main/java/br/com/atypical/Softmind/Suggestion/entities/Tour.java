package br.com.atypical.Softmind.Suggestion.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tour {

    private String tipoPasseio;
    private String descricaoPasseio;
    private String localPasseio;
}
