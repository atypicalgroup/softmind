package br.com.atypical.Softmind.Employee.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "EmployeeCreate", description = "DTO para criação/atualização de funcionários")
public record EmployeeCreateDto(

//        @Schema(description = "ID da empresa a que o funcionário pertence",
//                example = "64f8b2d9a1f23c0012a4e9cd")
//        String companyId,

        @Schema(description = "Nome completo do funcionário",
                example = "Maria da Silva")
        String name,

        @Schema(description = "E-mail corporativo do funcionário",
                example = "maria.silva@empresa.com.br")
        String email,

        @Schema(description = "Senha Inicial de acesso",
                example = "User@123")
        String password,

        @Schema(description = "Cargo ou função do funcionário",
                example = "Analista de RH")
        String role,

        @Schema(description = "Permissão de uso da aplicacão", example = "EMPLOYEE")
        String permission,

        @Schema(description = "Setor/departamento do funcionário",
                example = "Recursos Humanos")
        String sector
) {}
