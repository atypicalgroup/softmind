package br.com.atypical.Softmind.Survey.dto;

import br.com.atypical.Softmind.Movie.dto.MovieDto;
import br.com.atypical.Softmind.Survey.entities.SurveyResponse;

import java.util.List;

public record SurveyResponseWithMovies(
        SurveyResponse response,
        List<MovieDto> movies
) {}
