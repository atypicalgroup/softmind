package br.com.atypical.Softmind.Suggestion.service;

import br.com.atypical.Softmind.Suggestion.dto.MovieDto;

import br.com.atypical.Softmind.Survey.service.SurveyResponseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MovieService {

    private final ObjectMapper objectMapper;
    private final SurveyResponseService surveyResponseService;

    public MovieDto getMoviesForEmployee(String employeeId, String surveyId) throws Exception {
        String genreId = moodGenre(employeeId, surveyId);

        String url = "https://api.themoviedb.org/3/authentication" + "?with_genres=" + genreId + "&language=pt-BR";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer" + "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhOWYyY2E4YjZlZTBhNWUzOTI4YjhhZDM5ODc3ZGVjMCIsIm5iZiI6MTc1ODA2Mjg5Ny45MzYsInN1YiI6IjY4YzllOTMxZDA2ZGM0MjE2MGRlODBmZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.EFBQ5RJCX_AaDACxgMzWNgmzFf3Syc0DNvWNhOiMlB0")
                .build();

        try(Response response = new OkHttpClient().newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception(" Erro API TMDB " + response.message());
            }

            String json = response.body().string();
            return objectMapper.readValue(json, MovieDto.class);
        }


    }

    private String moodGenre(String employeeId, String surveyId) {
        String mood = surveyResponseService.getEmojiResponse(employeeId, surveyId);

        return switch (mood) {
            case "😊" -> "35"; // Comédia
            case "😢" -> "18"; // Drama
            case "😡" -> "28"; // Ação
            case "😱" -> "27"; // Terror
            default -> "10749"; // Romance
        };
    }


}
