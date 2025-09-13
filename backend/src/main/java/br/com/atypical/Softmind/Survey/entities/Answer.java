package br.com.atypical.Softmind.Survey.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    private String questionText;  // ou questionId, se você quiser referenciar
    private String response;
}
