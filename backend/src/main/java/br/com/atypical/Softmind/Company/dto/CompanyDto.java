package br.com.atypical.Softmind.Company.dto;

import br.com.atypical.Softmind.shared.enums.CompanyStatus;

import java.time.LocalDateTime;

public record CompanyDto(
        String id,
        String name,
        String cnpj,
        String email,
        String phone,
        AddressDto address,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        CompanyStatus status
) {
}
