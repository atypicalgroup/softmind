package br.com.atypical.Softmind.Support.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record SupportCreateDto(
        @Schema(description = "Nome do ponto de apoio", example = "Central de Atendimento de RH")
        String name,

        @Schema(description = "Descrição ou especialidade do ponto de apoio", example = "Atendimento relacionado a dúvidas de folha de pagamento e benefícios")
        String description,

        @Schema(description = "Números de contato disponíveis", example = "[\"+55 11 99999-9999\", \"+55 11 98888-8888\"]")
        String[] contactNumber
) {
}
