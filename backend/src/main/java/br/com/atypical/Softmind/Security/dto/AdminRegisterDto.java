package br.com.atypical.Softmind.Security.dto;

import br.com.atypical.Softmind.Company.dto.CompanyCreateDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para registrar um administrador")
public record AdminRegisterDto(
        @Schema(description = "Usuário (e-mail)", example = "admin@softmind.com")
        String username,

        @Schema(description = "Senha do administrador", example = "Admin@123")
        String password,

        @Schema(description = "Dados da empresa")
        CompanyCreateDto company
) {}
