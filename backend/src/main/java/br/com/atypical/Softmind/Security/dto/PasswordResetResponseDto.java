package br.com.atypical.Softmind.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "PasswordResetResponseDto",
        description = "Objeto de resposta genérico para operações de recuperação ou alteração de senha. Contém uma mensagem de status ou sucesso da operação."
)
public record PasswordResetResponseDto(

        @Schema(
                description = "Mensagem descritiva da resposta retornada pela API.",
                example = "Senha alterada com sucesso!",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String message
) {}
