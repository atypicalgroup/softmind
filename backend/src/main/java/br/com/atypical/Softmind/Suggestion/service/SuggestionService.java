package br.com.atypical.Softmind.Suggestion.service;

import br.com.atypical.Softmind.Movie.dto.MovieDto;
import br.com.atypical.Softmind.Movie.service.MovieService;
import br.com.atypical.Softmind.PhysicalActivity.service.PhysicalActService;
import br.com.atypical.Softmind.Support.service.SupportService;
import br.com.atypical.Softmind.Survey.entities.Answer;
import br.com.atypical.Softmind.Survey.entities.SurveyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SuggestionService {

    private final MovieService movieService;
    private final SupportService supportService;
    private final PhysicalActService physicalActService;

    public Map<String, Object> getSuggestionsByMood(String feeling) {
        var movies = movieService.getMoviesByFeeling(feeling);
        var supports = supportService.listAll();
        var activities = physicalActService.buscarVideosAtividadeFisica();

        return Map.of(
                "movies", movies,
                "supports", supports,
                "physicalActivities", activities
        );
    }
}