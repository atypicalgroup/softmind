package br.com.atypical.Softmind.Movie.dto;

public record MovieDto(
        Integer id,
        String title,
        String overview,
        String releaseDate,
        String posterUrl
) {}
