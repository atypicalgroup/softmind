package br.com.atypical.Softmind.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "PasswordResetRequestDto",
        description = "Objeto utilizado para solicitar a recuperação de senha. O usuário informa seu e-mail e recebe um token de 6 dígitos no endereço informado."
)
public record PasswordResetRequestDto(

        @Schema(
                description = "Endereço de e-mail do usuário que deseja recuperar a senha.",
                example = "usuario@softmind.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String email
) {}
