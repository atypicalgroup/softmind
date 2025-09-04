package br.com.atypical.Softmind.Company.dto;

public record AddressDto (
        String street,
        Integer number,
        String complement,
        String city,
        String state,
        String zip
) {
}
