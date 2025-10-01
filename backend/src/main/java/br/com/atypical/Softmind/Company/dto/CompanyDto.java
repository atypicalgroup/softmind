package br.com.atypical.Softmind.Company.dto;

import br.com.atypical.Softmind.shared.enums.CompanyStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "Company", description = "DTO de resposta com informações completas da empresa")
public record CompanyDto(

        @Schema(description = "Identificador único da empresa", example = "64f8b2d9a1f23c0012a4e9cd")
        String id,

        @Schema(description = "Nome da empresa", example = "Softmind Tecnologia LTDA")
        String name,

        @Schema(description = "CNPJ da empresa", example = "12.345.678/0001-90")
        String cnpj,

        @Schema(description = "E-mail de contato da empresa", example = "contato@softmind.com.br")
        String email,

        @Schema(description = "Telefone de contato da empresa", example = "+55 (11) 91234-5678")
        String phone,

        @Schema(description = "Data de criação do registro", example = "2025-09-02T10:15:30")
        LocalDateTime createdAt,

        @Schema(description = "Data da última atualização do registro", example = "2025-09-03T14:22:10")
        LocalDateTime updatedAt,

        @Schema(description = "Status atual da empresa", example = "ACTIVE")
        CompanyStatus status
) { }
