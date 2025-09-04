package br.com.atypical.Softmind.Company.mapper;

import br.com.atypical.Softmind.Company.dto.CompanyCreateDto;
import br.com.atypical.Softmind.Company.dto.CompanyDto;
import br.com.atypical.Softmind.Company.entities.Company;
import br.com.atypical.Softmind.shared.enums.CompanyStatus;

import java.time.LocalDateTime;

public class CompanyMapper {

    public static Company toEntity(CompanyCreateDto dto) {
        if (dto == null) return null;
        return new Company(
                null,
                dto.name(),
                dto.cnpj(),
                dto.email(),
                dto.phone(),
                AddressMapper.toEntity(dto.address()),
                LocalDateTime.now(),
                LocalDateTime.now(),
                CompanyStatus.ACTIVE
        );
    }

    public static CompanyDto toDto(Company entity) {
        if (entity == null) return null;
        return new CompanyDto(
                entity.getId(),
                entity.getName(),
                entity.getCnpj(),
                entity.getEmail(),
                entity.getPhone(),
                AddressMapper.toDto(entity.getAddress()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getStatus()
        );
    }
}
