package br.com.atypical.Softmind.Employee.controller;

import br.com.atypical.Softmind.Employee.dto.EmployeeCreateDto;
import br.com.atypical.Softmind.Employee.dto.EmployeeDto;
import br.com.atypical.Softmind.Employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@Tag(name = "3. Funcionários", description = "Gerenciamento de funcionários das empresas")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @Operation(
            summary = "Cria um novo funcionário",
            description = "Registra um funcionário vinculado a uma empresa.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Funcionário criado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Empresa não encontrada", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<EmployeeDto> create(@RequestBody EmployeeCreateDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @Operation(
            summary = "Lista todos os funcionários",
            description = "Retorna a lista completa de funcionários cadastrados no sistema."
    )
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Busca funcionário por ID",
            description = "Retorna os dados de um funcionário específico pelo seu ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Funcionário encontrado"),
                    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Lista funcionários por empresa",
            description = "Retorna todos os funcionários vinculados a uma empresa específica."
    )
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<EmployeeDto>> getByCompany(@PathVariable String companyId) {
        return ResponseEntity.ok(service.findByCompanyId(companyId));
    }

    @Operation(
            summary = "Busca funcionário por e-mail",
            description = "Retorna os dados de um funcionário a partir do seu e-mail.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Funcionário encontrado"),
                    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado", content = @Content)
            }
    )
    @GetMapping("/email/{email}")
    public ResponseEntity<EmployeeDto> getByEmail(@PathVariable String email) {
        return service.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Atualiza dados de um funcionário",
            description = "Atualiza as informações de um funcionário existente.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Funcionário atualizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado", content = @Content)
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> update(@PathVariable String id, @RequestBody EmployeeCreateDto dto) {
        return service.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Remove um funcionário",
            description = "Exclui um funcionário do sistema.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Funcionário removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
