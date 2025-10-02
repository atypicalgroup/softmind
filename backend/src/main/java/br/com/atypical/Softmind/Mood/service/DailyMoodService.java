package br.com.atypical.Softmind.Mood.service;

import br.com.atypical.Softmind.Mood.dto.DailyMoodRequestDto;
import br.com.atypical.Softmind.Mood.entities.DailyMood;
import br.com.atypical.Softmind.Mood.repository.DailyMoodRepository;
import br.com.atypical.Softmind.Movie.service.MovieService;
import br.com.atypical.Softmind.Movie.dto.MovieDto;
import br.com.atypical.Softmind.Suggestion.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DailyMoodService {

    private final DailyMoodRepository repository;
    private final SuggestionService suggestionService;

    public Map<String, Object> saveAndRecommend(String companyId, String employeeId, DailyMoodRequestDto dto) {
        DailyMood mood = new DailyMood();
        mood.setCompanyId(companyId);
        mood.setEmployeeId(employeeId);
        mood.setEmoji(dto.emoji());
        mood.setFeeling(dto.feeling());
        repository.save(mood);

        var suggestions = suggestionService.getSuggestionsByMood(dto.feeling());

        return Map.of(
                "emoji", dto.emoji(),
                "feeling", dto.feeling(),
                "recommendations", suggestions
        );
    }

}
