package br.com.atypical.Softmind.Company.dto;

public record CompanyCreateDto(
        String name,
        String cnpj,
        String phone,
        String email,
        AddressDto address
) {
}
