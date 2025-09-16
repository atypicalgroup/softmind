package br.com.atypical.Softmind.security.dto;

import br.com.atypical.Softmind.shared.enums.Permission;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta após registro de administrador")
public record AdminResponseDto(
        @Schema(description = "ID do usuário", example = "66dfaa10c0d1a45a6e2b1234")
        String id,

        @Schema(description = "Usuário (e-mail)", example = "admin@softmind.com")
        String username,

        @Schema(description = "Perfil do usuário", example = "ADMIN")
        Permission permission
) {}
