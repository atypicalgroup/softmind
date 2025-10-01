package br.com.atypical.Softmind.PhysicalActivity.service;

import br.com.atypical.Softmind.PhysicalActivity.dto.PhysicalActDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class PhysicalActService {

    private final RestTemplate restTemplate;

    private static final String YOUTUBE_API_URL = "https://www.googleapis.com/youtube/v3/search";
    private static final String API_KEY = "AIzaSyB8myycL8s2sPJO5XAEOX1Mndeqb4LKuvU";

    public PhysicalActService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<PhysicalActDto> buscarVideosAtividadeFisica() {

        String url = UriComponentsBuilder.fromHttpUrl(YOUTUBE_API_URL)
                .queryParam("part", "snippet")
                .queryParam("type", "video")
                .queryParam("maxResults", 5)
                .queryParam("q", "exercicios alongamento iniciantes")
                .queryParam("regionCode", "BR")
                .queryParam("revelanceLanguage", "pt")
                .queryParam("key", API_KEY)
                .toUriString();

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        Map<String, Object> body = response.getBody();

        if (body == null || !body.containsKey("items")) {
            return List.of();
        }

        List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");

        return items.stream()
                .map(item -> {
                    Map<String, Object> id = (Map<String, Object>) item.get("id");
                    Map<String, Object> snippet = (Map<String, Object>) item.get("snippet");
                    Map<String, Object> thumbnails = (Map<String, Object>) snippet.get("thumbnails");
                    Map<String, Object> thumbDefault = (Map<String, Object>) thumbnails.get("default");

                    String videoId = (String) id.get("videoId");

                    String videoLink = "https://www.youtube.com/watch?v=" + videoId;

                    return new PhysicalActDto(
                            (String) id.get("videoId"),
                            (String) snippet.get("title"),
                            (String) snippet.get("description"),
                            (String) thumbDefault.get("url"),
                            videoLink
                    );
                })
                .toList();
    }
}


//curl "https://www.googleapis.com/youtube/v3/search?part=snippet&q=exercicios&type=video&maxResults=5&key=AIzaSyCsjFNSZNxSm4tKfkeUcJeb9-0e6V361-s"

