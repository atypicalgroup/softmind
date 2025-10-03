package br.com.atypical.Softmind.Movie.service;

import br.com.atypical.Softmind.Movie.dto.MovieDto;
import br.com.atypical.Softmind.Movie.dto.TmdbResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.repository.query.Param;
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

    //Será implementado melhoria de segurança para ocultar token abaixo
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

//    private String mapFeelingToGenreId(String sentimento) {
//        return switch (sentimento.toLowerCase()) {
//            case "sad" -> "35,10749,10751,14,16";
//            case "anxious" -> "35,16,10751,10402,14";
//            case "anger" -> "28,53,80,18";
//            case "happy" -> "35,10751,10402,12";
//            case "fear" -> "27,9648";
//            case "tired" -> "99,35,10751";
//            default -> "14,12,35";
//        };
//    }

    // MovieService.java

    private String mapFeelingToGenreId(String sentimento) {
        return switch (sentimento.toLowerCase()) {
            case "triste" -> "35,10749,10751,14,16";
            case "ansioso" -> "35,16,10751,10402,14";
            case "raiva" -> "28,53,80,18"; // Ação/Crime
            case "alegre" -> "35,10751,10402,12"; // Comédia, Família
            case "medo" -> "27,9648"; // Terror, Mistério
            case "cansado" -> "99,35,10751"; // Documentário, Comédia (para relaxar)
            default -> "14,12,35"; // Default (Fantasia, Aventura, Comédia)
        };
    }


}
