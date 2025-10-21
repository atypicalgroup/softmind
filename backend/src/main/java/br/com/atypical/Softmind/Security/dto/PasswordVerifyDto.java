package br.com.atypical.Softmind.Security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "PasswordVerifyDto",
        description = "Objeto utilizado para validação do token de recuperação de senha. Contém o e-mail do usuário e o token recebido por e-mail."
)
public record PasswordVerifyDto(

        @Schema(
                description = "Endereço de e-mail do usuário que solicitou a recuperação de senha.",
                example = "usuario@softmind.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String email,

        @Schema(
                description = "Código de verificação de 6 dígitos enviado para o e-mail do usuário.",
                example = "123456",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String token
) {}
