package br.com.atypical.Softmind.PhysicalActivity.controller;

import br.com.atypical.Softmind.PhysicalActivity.dto.PhysicalActDto;
import br.com.atypical.Softmind.PhysicalActivity.service.PhysicalActService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/physical-activities")
@RequiredArgsConstructor
@Tag(name = "Physical Activities", description = "Recursos relacionados a atividades físicas e vídeos de apoio")
public class PhysicalActController {

    private final PhysicalActService physicalActService;

    @Operation(
            summary = "Lista vídeos de atividades físicas",
            description = "Retorna uma lista de vídeos de atividades físicas disponíveis para os colaboradores.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de vídeos retornada com sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PhysicalActDto.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<PhysicalActDto>> getVideosDeAtividades() {
        return ResponseEntity.ok(physicalActService.buscarVideosAtividadeFisica());
    }
}
