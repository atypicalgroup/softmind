package br.com.atypical.Softmind.Company.controller;

import br.com.atypical.Softmind.Company.dto.CompanyCreateDto;
import br.com.atypical.Softmind.Company.dto.CompanyDto;
import br.com.atypical.Softmind.Company.entities.Company;
import br.com.atypical.Softmind.Company.mapper.CompanyMapper;
import br.com.atypical.Softmind.Company.repository.CompanyRepository;
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
@RequestMapping("/companies")
@RequiredArgsConstructor
@Tag(name = "Empresa", description = "Gerenciamento das empresas cadastradas no sistema gerenciado pelos administradores")
@PreAuthorize("hasRole('ADMIN')")
public class CompanyController {

    private final CompanyService service;
    private final CompanyRepository companyRepository;

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
        Company created = service.create(dto);
        return ResponseEntity.ok(CompanyMapper.toDto(created));
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
