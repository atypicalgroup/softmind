package br.com.atypical.Softmind.Psychologist.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Psychologist {

    private Integer id;
    private String name;
    private String description;
    private String[] contactNumber;
}
