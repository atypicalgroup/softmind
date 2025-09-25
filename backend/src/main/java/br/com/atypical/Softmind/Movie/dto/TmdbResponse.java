package br.com.atypical.Softmind.Movie.dto;

import lombok.Data;

import java.util.List;

@Data
public class TmdbResponse {
    private List<TmdbMovie> results;
}
