package br.com.atypical.Softmind.Suggestion.service;

import br.com.atypical.Softmind.Movie.dto.MovieDto;
import br.com.atypical.Softmind.Movie.service.MovieService;
import br.com.atypical.Softmind.Survey.entities.Answer;
import br.com.atypical.Softmind.Survey.entities.SurveyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SuggestionService {

    private final MovieService movieService;

    public List<MovieDto> suggestMovies(SurveyResponse response) {
        // 1. Extrair sentimento da primeira resposta
        String feeling = response.getAnswers().stream()
                .findFirst()
                .map(Answer::getResponse)
                .orElseThrow(() -> new RuntimeException("Resposta da pergunta 1 não encontrada"));

        // 2. Consultar filmes pelo sentimento
        return movieService.getMoviesByFeeling(feeling);
    }
}
