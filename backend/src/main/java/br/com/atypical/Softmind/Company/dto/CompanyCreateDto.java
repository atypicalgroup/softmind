package br.com.atypical.Softmind.Company.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CompanyCreate", description = "DTO para criação/atualização de empresas")
public record CompanyCreateDto(

        @Schema(description = "Nome da empresa", example = "Softmind Tecnologia LTDA")
        String name,

        @Schema(description = "CNPJ da empresa", example = "12.345.678/0001-90")
        String cnpj,

        @Schema(description = "Telefone de contato da empresa", example = "+55 (11) 91234-5678")
        String phone,

        @Schema(description = "E-mail de contato da empresa", example = "contato@softmind.com.br")
        String email
) { }
