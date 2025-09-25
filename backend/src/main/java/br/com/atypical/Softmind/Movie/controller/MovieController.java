package br.com.atypical.Softmind.Movie.controller;

import br.com.atypical.Softmind.Movie.dto.MovieDto;
import br.com.atypical.Softmind.Movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    /**
     * Testa busca de filmes por sentimento
     * Exemplo de chamada:
     *   GET /api/movies/suggestions?sentimento=feliz
     */
    @GetMapping("/suggestions")
    public ResponseEntity<List<MovieDto>> getSuggestions(@RequestParam String sentimento) {
        List<MovieDto> movies = movieService.getMoviesByFeeling(sentimento);
        return ResponseEntity.ok(movies);
    }
}
