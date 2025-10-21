package br.com.atypical.Softmind.Security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta da autenticação")
public record LoginResponseDto(

        @Schema(description = "Token JWT gerado", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,

        @Schema(description = "Usuário autenticado (e-mail)", example = "maria.silva@empresa.com.br")
        String username,

        @Schema(description = "Nome do funcionário", example = "Maria da Silva")
        String name
) {}
