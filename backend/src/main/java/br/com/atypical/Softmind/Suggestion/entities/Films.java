package br.com.atypical.Softmind.Suggestion.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Films {

    private String nomeFilme;
    private String categoriaFilme;

    //necessito resgatar algum atributo de survey para logica de humor e filmes
}
