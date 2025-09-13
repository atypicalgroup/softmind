package br.com.atypical.Softmind.Survey.entities;

import br.com.atypical.Softmind.shared.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    private String text;
    private QuestionType type;
    private String[] options;

}
