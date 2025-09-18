package br.com.atypical.Softmind.Suggestion.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Channel {

    private String name;
    private String description;
    private String[] contactNumber;
}
