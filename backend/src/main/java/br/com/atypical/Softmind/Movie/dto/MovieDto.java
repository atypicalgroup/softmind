package br.com.atypical.Softmind.Movie.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Filme retornado pela recomendação de acordo com o sentimento do colaborador")
public record MovieDto(

        @Schema(description = "ID do filme no TMDB", example = "550")
        Integer id,

        @Schema(description = "Título do filme", example = "Clube da Luta")
        String title,

        @Schema(description = "Resumo ou sinopse do filme", example = "Um insone e um vendedor de sabonetes formam um clube de luta clandestino que evolui para algo muito maior.")
        String overview,

        @Schema(description = "Data de lançamento do filme", example = "1999-10-15")
        String releaseDate,

        @Schema(description = "URL do poster do filme", example = "https://image.tmdb.org/t/p/w500/abc123poster.jpg")
        String posterUrl
) {}

