package br.com.atypical.Softmind.Tour.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tour {

    private String nameTour;
    private String typeTour;
    private String descriptionTour;
    private String placeTour;
}
