package br.com.atypical.Softmind.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta da autenticação")
public record LoginResponseDto(

        @Schema(description = "JWT para uso nas próximas requisições")
        String token,

        @Schema(description = "Usuário autenticado", example = "admin@softmind.com")
        String username
) {}
