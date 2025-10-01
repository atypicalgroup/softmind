package br.com.atypical.Softmind.Suggestion.entities;

import br.com.atypical.Softmind.Movie.entities.Movie;
import br.com.atypical.Softmind.Psychologist.entities.Psychologist;
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
    private List<Psychologist> psychologist;
    private List<Movie> movie;
    private List<PhysicalActivity> physicalActivity;

}
