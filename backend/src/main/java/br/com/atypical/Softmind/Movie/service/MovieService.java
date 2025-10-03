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
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final RestTemplate restTemplate;

    @Value("${security.token.tmdb}")
    private String apiKey;

    private static final String TMDB_URL = "https://api.themoviedb.org/3/discover/movie";

    // 🎭 Constantes de gêneros
    private static final String COMEDIA_ROMANCE_FAMILIA = "35,10749,10751";
    private static final String ANIMACAO_FAMILIA_MUSICA_FANTASIA = "16,10751,10402,14";
    private static final String ACAO_CRIME_DRAMA = "28,53,80,18";
    private static final String AVENTURA_COMEDIA_FAMILIA = "12,35,10751";
    private static final String ACOLHEDOR_FANTASIA = "16,35,10751,14"; // usado para "medo"
    private static final String DOCUMENTARIO_RELAX = "99,35,10751";
    private static final String PADRAO = "14,12,35"; // fantasia/aventura/comédia

    // 🌍 Mapa multilíngue: português + inglês
    private static final Map<String, String> FEELING_TO_GENRE = Map.ofEntries(
            Map.entry("triste", COMEDIA_ROMANCE_FAMILIA),
            Map.entry("sad", COMEDIA_ROMANCE_FAMILIA),

            Map.entry("ansioso", ANIMACAO_FAMILIA_MUSICA_FANTASIA),
            Map.entry("anxious", ANIMACAO_FAMILIA_MUSICA_FANTASIA),

            Map.entry("raiva", ACAO_CRIME_DRAMA),
            Map.entry("angry", ACAO_CRIME_DRAMA),
            Map.entry("anger", ACAO_CRIME_DRAMA),

            Map.entry("alegre", AVENTURA_COMEDIA_FAMILIA),
            Map.entry("happy", AVENTURA_COMEDIA_FAMILIA),
            Map.entry("joyful", AVENTURA_COMEDIA_FAMILIA),

            Map.entry("medo", ACOLHEDOR_FANTASIA),
            Map.entry("fear", ACOLHEDOR_FANTASIA),
            Map.entry("scared", ACOLHEDOR_FANTASIA),

            Map.entry("cansado", DOCUMENTARIO_RELAX),
            Map.entry("tired", DOCUMENTARIO_RELAX),
            Map.entry("sleepy", DOCUMENTARIO_RELAX)
    );

    public List<MovieDto> getMoviesByFeeling(String sentimento) {
        String genreId = mapFeelingToGenreId(sentimento);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(TMDB_URL)
                .queryParam("language", "pt-BR")
                .queryParam("with_genres", genreId)
                .queryParam("sort_by", "popularity.desc")
                .queryParam("page", 1);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);

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
        if (sentimento == null) return PADRAO;
        return FEELING_TO_GENRE.getOrDefault(sentimento.toLowerCase(), PADRAO);
    }
}
