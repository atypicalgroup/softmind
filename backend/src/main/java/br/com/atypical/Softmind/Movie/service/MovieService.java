package br.com.atypical.Softmind.Movie.service;

import br.com.atypical.Softmind.Movie.dto.MovieDto;
import br.com.atypical.Softmind.Movie.dto.TmdbResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MovieService {

    private final RestTemplate restTemplate;


    private static final String API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhOWYyY2E4YjZlZTBhNWUzOTI4YjhhZDM5ODc3ZGVjMCIsIm5iZiI6MTc1ODA2Mjg5Ny45MzYsInN1YiI6IjY4YzllOTMxZDA2ZGM0MjE2MGRlODBmZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.EFBQ5RJCX_AaDACxgMzWNgmzFf3Syc0DNvWNhOiMlB0";
    private static final String TMDB_URL = "https://api.themoviedb.org/3/discover/movie";

    public List<MovieDto> getMoviesByFeeling(String sentimento) {
        String genreId = mapFeelingToGenreId(sentimento);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(TMDB_URL)
                .queryParam("language", "pt-BR")
                .queryParam("with_genres", genreId)
                .queryParam("sort_by", "popularity.desc")
                .queryParam("page", 1);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(API_KEY); // <- aqui entra o token v4 do .env

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<TmdbResponse> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        if (response.getBody() == null || response.getBody().getResults() == null) {
            return List.of();
        }

        return response.getBody().getResults()
                .stream()
                .limit(5)
                .map(movie -> new MovieDto(
                        movie.getId(),
                        movie.getTitle(),
                        movie.getOverview(),
                        movie.getReleaseDate(),
                        "https://image.tmdb.org/t/p/w500" + movie.getPosterPath()
                ))
                .toList();
    }


    private String mapFeelingToGenreId(String sentimento) {
        return switch (sentimento.toLowerCase()) {
            case "feliz" -> "35";  // Comédia
            case "triste" -> "18"; // Drama
            case "ansioso" -> "28"; // Ação
            case "entediado" -> "12"; // Aventura
            case "romântico" -> "10749"; // Romance
            default -> "14"; // Fantasia (fallback)
        };
    }

}
