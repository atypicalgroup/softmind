package br.com.atypical.Softmind.Suggestion.controller;

import br.com.atypical.Softmind.Suggestion.dto.TourDto;
import br.com.atypical.Softmind.Suggestion.service.TourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suggestion/tour")
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;

    @Operation(
            summary = "Cadastrar novo passeio",
            description = "Criar novo passeio com base no humor do funcionário"
    )
    @ApiResponse(responseCode = "201", description = "Passeio criado com sucesso!",
    content = @Content(schema = @Schema(implementation = TourDto.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TourDto criarPasseio(@RequestBody @Valid TourDto tourDto){
        return tourService.cadastrarPasseio(tourDto);
    }

    @Operation(
            summary = "Listar todos os passeios",
            description = "Retorna todos os passeios cadastrados"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de passeios retornada com sucesso",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TourDto.class))
            )
    )
    @GetMapping
    public List<TourDto> listarPasseios(){
        return tourService.listarPasseios();
    }

    @Operation(
            summary = "Excluir passeio",
            description = "Excluir passeio"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Passeio excluído com sucesso"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Passeio não encontrado"
    )
    @DeleteMapping("/{nameTour}")
    public ResponseEntity<Void> excluir (@PathVariable String nameTour){
        boolean excluido = tourService.excluirPasseio(nameTour);
        if(excluido){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Atualizar passeio",
            description = "Atualizar passeio por nome, tipo, descrição ou local"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Passeio atualizado com sucesso"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Passeio não encontrado"
    )
    @PutMapping("/{nameTour}")
    public ResponseEntity<TourDto> atualizarPasseio(@PathVariable String nameTour, @RequestBody @Valid TourDto tourDto){
        return ResponseEntity.ok(tourService.atualizarTour(nameTour, tourDto));
    }
}
