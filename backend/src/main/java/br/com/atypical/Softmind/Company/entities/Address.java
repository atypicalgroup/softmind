package br.com.atypical.Softmind.Company.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String street;
    private Integer number;
    private String complement;
    private String city;
    private String state;
    private String zip;
}
