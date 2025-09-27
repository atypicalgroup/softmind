package br.com.atypical.Softmind.Psychologist.controller;

import br.com.atypical.Softmind.Psychologist.dto.PsychologistDto;
import br.com.atypical.Softmind.Psychologist.service.PsychologistService;
import br.com.atypical.Softmind.Tour.dto.TourDto;
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
@RequestMapping("/suggestion/psychologist")
@RequiredArgsConstructor
public class PsychologistController {

    private final PsychologistService psychologistService;

    @Operation(
            summary = "Cadastrar novo psicólogo",
            description = "Criar psicólogo da empresa como sugestão ao usuário"
    )
    @ApiResponse(responseCode = "201", description = "Psicólogo criado com sucesso!",
            content = @Content(schema = @Schema(implementation = PsychologistDto.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PsychologistDto criarPsicologo(@RequestBody @Valid PsychologistDto psychologistDto){
        return psychologistService.savePsychologist(psychologistDto);
    }



    @Operation(
            summary = "Excluir psicólogo",
            description = "Excluir psicólogo"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Psicólogo excluído com sucesso"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Psicólogo não encontrado"
    )
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> excluirPsicologo (@PathVariable String name){
        boolean excluido = psychologistService.deletePsychologist(name);
        if(excluido){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Atualizar psicólogo",
            description = "Atualizar psicólogo por nome, descrição e contato"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Psicólogo atualizado com sucesso"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Psicólogo não encontrado"
    )
    @PutMapping("/{name}")
    public ResponseEntity<PsychologistDto> atualizarPsicologo(@PathVariable String name, @RequestBody @Valid PsychologistDto psychologistDto){
        return ResponseEntity.ok(psychologistService.updatePsychologist(name, psychologistDto));
    }


}
