package br.com.atypical.Softmind.Movie.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    private Integer id;
    private String title;
    private String overview;
    private String releaseDate;
    private String posterUrl;

    //necessito resgatar algum atributo de survey para logica de humor e filmes
}
