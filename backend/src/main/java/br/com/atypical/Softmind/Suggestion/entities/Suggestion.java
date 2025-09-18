package br.com.atypical.Softmind.Suggestion.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tb_suggestion")
public class Suggestion {

    @Id
    private String idSuggestion;
    private List<Channel> psychologist;
    private List<Films> film;
    private List<PhysicalActivity> physicalActivity;
    private List<Tour> tour;

}
