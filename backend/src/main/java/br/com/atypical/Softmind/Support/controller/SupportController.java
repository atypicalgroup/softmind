package br.com.atypical.Softmind.Support.controller;

import br.com.atypical.Softmind.Support.dto.SupportCreateDto;
import br.com.atypical.Softmind.Support.entities.Support;
import br.com.atypical.Softmind.Support.service.SupportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supports")
@RequiredArgsConstructor
public class SupportController {

    private final SupportService service;

    @Operation(
            summary = "Cria um novo ponto de apoio",
            description = "Permite ao administrador cadastrar um novo ponto de apoio da empresa.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Ponto de apoio criado com sucesso",
                            content = @Content(schema = @Schema(implementation = Support.class))),
                    @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados", content = @Content)
            },
            tags = "Administração"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Support> create(@RequestBody SupportCreateDto dto) {
        return ResponseEntity.status(201).body(service.create(dto));
    }

    @Operation(
            summary = "Lista todos os pontos de apoio",
            description = "Retorna a lista completa de pontos de apoio cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de pontos de apoio retornada com sucesso",
                            content = @Content(schema = @Schema(implementation = Support.class)))
            },
            tags = "Funcionários"
    )
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    @GetMapping("/support-list")
    public ResponseEntity<List<Support>> listSupportAll() {
        return ResponseEntity.ok(service.listSupportAll());
    }
    @Operation(
            summary = "Lista todos os pontos de apoio",
            description = "Retorna a lista completa de pontos de apoio cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de pontos de apoio retornada com sucesso",
                            content = @Content(schema = @Schema(implementation = Support.class)))
            },
            tags = "Administração"
    )
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Support>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }


    @Operation(
            summary = "Atualiza um ponto de apoio existente",
            description = "Atualiza os dados de um ponto de apoio pelo ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ponto de apoio atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = Support.class))),
                    @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Ponto de apoio não encontrado", content = @Content)
            },
            tags = "Administração"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Support> update(
            @PathVariable String id,
            @RequestBody SupportCreateDto dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
            summary = "Deleta um ponto de apoio",
            description = "Remove um ponto de apoio existente pelo ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Ponto de apoio deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Ponto de apoio não encontrado", content = @Content)
            },
            tags = "Administração"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
