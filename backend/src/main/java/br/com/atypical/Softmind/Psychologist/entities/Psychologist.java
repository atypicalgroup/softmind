package br.com.atypical.Softmind.Psychologist.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "gs_tb_psychologists")
public class Psychologist {

    private String id;
    private String name;
    private String description;
    private String[] contactNumber;
}
