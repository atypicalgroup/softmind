package br.com.atypical.Softmind.Company.controller;

import br.com.atypical.Softmind.Company.dto.CompanyCreateDto;
import br.com.atypical.Softmind.Company.dto.CompanyDto;
import br.com.atypical.Softmind.Company.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Tag(name = "Empresa", description = "Gerenciamento das empresas cadastradas no sistema gerenciado pelos administradores")
@PreAuthorize("hasRole('ADMIN')")
public class CompanyController {

    private final CompanyService service;

    @Operation(
            summary = "Cria uma nova empresa",
            description = "Registra uma nova empresa no sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Empresa criada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<CompanyDto> create(@RequestBody CompanyCreateDto dto) {
        CompanyDto created = service.create(dto);
        return ResponseEntity.ok(created);
    }

    @Operation(
            summary = "Lista todas as empresas",
            description = "Retorna a lista completa de empresas cadastradas."
    )
    @GetMapping
    public ResponseEntity<List<CompanyDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Busca empresa por ID",
            description = "Retorna os dados de uma empresa específica pelo seu ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Empresa encontrada"),
                    @ApiResponse(responseCode = "404", description = "Empresa não encontrada", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Busca empresa por CNPJ",
            description = "Retorna os dados de uma empresa a partir do CNPJ."
    )
    @GetMapping("/{cnpj}")
    public ResponseEntity<CompanyDto> getByCnpj(@PathVariable String cnpj) {
        return service.findByCnpj(cnpj)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Busca empresa por nome",
            description = "Retorna os dados de uma empresa a partir do nome."
    )
    @GetMapping("/{name}")
    public ResponseEntity<CompanyDto> getByName(@PathVariable String name) {
        return service.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Busca empresa por e-mail",
            description = "Retorna os dados de uma empresa a partir do e-mail cadastrado."
    )
    @GetMapping("/{email}")
    public ResponseEntity<CompanyDto> getByEmail(@PathVariable String email) {
        return service.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Atualiza dados de uma empresa",
            description = "Atualiza as informações de uma empresa existente.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Empresa atualizada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Empresa não encontrada", content = @Content)
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CompanyDto> update(@PathVariable String id,
                                             @RequestBody CompanyCreateDto dto) {
        return service.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Remove uma empresa",
            description = "Exclui uma empresa do sistema.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Empresa removida com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Empresa não encontrada", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
