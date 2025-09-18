package br.com.atypical.Softmind.Suggestion.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalActivity {

    private Double tempoAtv;
    private String nomeAtv;
    private String tipoAtv;
    private String descricaoAtv;

    //api yt sobre atividades e exercicios fisicos
}
