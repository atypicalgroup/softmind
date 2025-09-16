package br.com.atypical.Softmind.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para registrar um administrador")
public record AdminRegisterDto(
        @Schema(description = "Usuário (e-mail)", example = "admin@softmind.com")
        String username,

        @Schema(description = "Senha do administrador", example = "Admin@123")
        String password
) {}
