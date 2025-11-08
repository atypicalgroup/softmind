package br.com.atypical.Softmind.Security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "PasswordChangeDto",
        description = "Objeto usado para alteração de senha, contendo o e-mail, token de verificação e nova senha."
)
public record PasswordChangeDto(

        @Schema(
                description = "Endereço de e-mail do usuário que solicitou a redefinição de senha.",
                example = "usuario@softmind.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String email,

        @Schema(
                description = "Código de verificação de 6 dígitos recebido por e-mail.",
                example = "123456",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String token,

        @Schema(
                description = "Nova senha que substituirá a anterior.",
                example = "NovaSenhaSegura123",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String newPassword
) {}
