package br.com.atypical.Softmind.Tour.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TourDto(
        @Schema(
                description = "Nome do passeio",
                example = "Parque Ibirapuera"
        )
        String nameTour,

        @Schema(
                    description = "Cadastrar o tipo de passeio",
                    example = "Parque"
        )
        String typeTour,

        @Schema(
                description = "Descrição do passeio",
                example = "Passeio ou caminhada em parques"
        )
        String descriptionTour,

        @Schema(
                description = "Local do passeio",
                example = "Avenida São João 1234 - São Paulo"
        )
        String placeTour
) {
}
