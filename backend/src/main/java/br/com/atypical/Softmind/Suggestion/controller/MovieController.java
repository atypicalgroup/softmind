package br.com.atypical.Softmind.Suggestion.controller;

import br.com.atypical.Softmind.Suggestion.dto.MovieDto;
import br.com.atypical.Softmind.Suggestion.service.MovieService;
import br.com.atypical.Softmind.Survey.dto.SurveyDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/suggestions/movie")
@RequiredArgsConstructor
@Tag(name = "Sugestão", description = "Sugestão de filmes para o usuário conforme resposta")
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/{employeeId}/{surveyId}")
    public ResponseEntity<MovieDto> getMovie(@PathVariable String employeeId, @PathVariable String surveyId) {
        try {
            MovieDto movie = movieService.getMoviesForEmployee(employeeId, surveyId);
            return ResponseEntity.ok(movie);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }



}
