package br.com.atypical.Softmind.Movie.mapper;

import br.com.atypical.Softmind.Movie.dto.MovieDto;
import br.com.atypical.Softmind.Movie.entities.Movie;

public class MovieMapper {

    private MovieDto toDto(Movie movie) {
        return new MovieDto(
                movie.getId(),
                movie.getTitle(),
                movie.getOverview(),
                movie.getReleaseDate(),
                movie.getPosterUrl()
        );
    }

}
