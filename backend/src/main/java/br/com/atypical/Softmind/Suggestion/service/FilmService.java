package br.com.atypical.Softmind.Suggestion.service;

import br.com.atypical.Softmind.Suggestion.dto.FilmsDto;
import br.com.atypical.Softmind.Survey.service.SurveyResponseService;
import br.com.atypical.Softmind.Survey.service.SurveyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FilmService {

    private final ObjectMapper objectMapper;
    private final SurveyResponseService surveyResponseService;

    public FilmsDto getMoviesByMood(String mood) throws Exception {
        String genreId = moodGenre(mood);

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
            return objectMapper.readValue(json, FilmsDto.class);
        }


    }

    private String moodGenre(SurveyResponseService surveyResponseService) throws Exception {

    }

//    private String moodGenre(String mood) {
//        return switch (mood.toLowerCase()){
//            case "😀" -> "10402";
//            case "😡" -> "10751";
//            default -> "35";
//        };
//
//
//        //preciso adicionar a surveyservice e respostas vindo delas
//    }


}
