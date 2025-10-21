package br.com.atypical.Softmind.Security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Credenciais para autenticação de usuário")
public record LoginRequestDto(

        @Schema(description = "Usuário (e-mail ou username)", example = "admin@softmind.com")
        String username,

        @Schema(description = "Senha do usuário", example = "Admin@123")
        String password
) {}
