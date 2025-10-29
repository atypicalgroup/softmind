package br.com.atypical.Softmind.Survey.dto;

import br.com.atypical.Softmind.Movie.dto.MovieDto;
import br.com.atypical.Softmind.Survey.entities.SurveyResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        name = "SurveyResponseWithMovies",
        description = "Retorno combinado contendo a resposta da pesquisa diária e as recomendações de filmes relacionadas ao estado emocional do colaborador."
)
public record SurveyResponseWithMovies(

        @Schema(
                description = "Dados da resposta submetida pelo colaborador na pesquisa diária.",
                implementation = SurveyResponse.class
        )
        SurveyResponse response,

        @Schema(
                description = "Lista de recomendações de filmes baseadas nas respostas do colaborador.",
                example = """
                        [
                          {
                            "title": "Divertida Mente",
                            "genre": "Animação / Comédia",
                            "year": 2015,
                            "description": "Uma animação que explora as emoções humanas de forma leve e educativa.",
                            "imageUrl": "https://image.tmdb.org/t/p/w500/inside_out.jpg"
                          },
                          {
                            "title": "O Fabuloso Destino de Amélie Poulain",
                            "genre": "Drama / Romance",
                            "year": 2001,
                            "description": "Um retrato otimista e sensível sobre as pequenas alegrias da vida.",
                            "imageUrl": "https://image.tmdb.org/t/p/w500/amelie.jpg"
                          }
                        ]
                        """
        )
        List<MovieDto> movies
) {}
