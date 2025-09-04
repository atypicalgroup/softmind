package br.com.atypical.Softmind.Employee.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "Employee", description = "DTO de resposta com informações completas do funcionário")
public record EmployeeDto(

        @Schema(description = "Identificador único do funcionário",
                example = "6500c2f9e1a8c70012d4b9ef")
        String id,

        @Schema(description = "ID da empresa a que o funcionário pertence",
                example = "64f8b2d9a1f23c0012a4e9cd")
        String companyId,

        @Schema(description = "Nome completo do funcionário",
                example = "Maria da Silva")
        String name,

        @Schema(description = "E-mail corporativo do funcionário",
                example = "maria.silva@empresa.com.br")
        String email,

        @Schema(description = "Cargo ou função do funcionário",
                example = "Analista de RH")
        String role,

        @Schema(description = "Setor/departamento do funcionário",
                example = "Recursos Humanos")
        String sector,

        @Schema(description = "Data de criação do registro",
                example = "2025-09-02T10:15:30")
        LocalDateTime createdAt,

        @Schema(description = "Data da última atualização do registro",
                example = "2025-09-03T14:22:10")
        LocalDateTime updatedAt,

        @Schema(description = "Status ativo/inativo do funcionário",
                example = "true")
        boolean active
) {}
