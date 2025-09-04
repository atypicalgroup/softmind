package br.com.atypical.Softmind.Company.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Address", description = "Endereço da empresa")
public record AddressDto(

        @Schema(description = "Nome da rua/avenida", example = "Av. Paulista")
        String street,

        @Schema(description = "Número do endereço", example = "1000")
        Integer number,

        @Schema(description = "Complemento do endereço", example = "Conjunto 1203")
        String complement,

        @Schema(description = "Cidade do endereço", example = "São Paulo")
        String city,

        @Schema(description = "Estado do endereço (UF)", example = "SP")
        String state,

        @Schema(description = "CEP no formato 00000-000", example = "01310-100")
        String zip
) { }
