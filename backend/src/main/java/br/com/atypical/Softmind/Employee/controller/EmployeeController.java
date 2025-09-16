package br.com.atypical.Softmind.Employee.controller;

import br.com.atypical.Softmind.Employee.dto.EmployeeCreateDto;
import br.com.atypical.Softmind.Employee.dto.EmployeeDto;
import br.com.atypical.Softmind.Employee.service.EmployeeService;
import br.com.atypical.Softmind.Survey.dto.SurveyResponseCreateDto;
import br.com.atypical.Softmind.Survey.entities.SurveyResponse;
import br.com.atypical.Softmind.Survey.service.SurveyResponseService;
import br.com.atypical.Softmind.Survey.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final SurveyResponseService surveyResponseService;

    @Operation(
            summary = "Cria um novo funcionário",
            description = "Registra um funcionário vinculado a uma empresa.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Funcionário criado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Empresa não encontrada", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
            },
            tags = "Administração"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EmployeeDto> create(@RequestBody EmployeeCreateDto dto) {
        return ResponseEntity.ok(employeeService.create(dto));
    }

    @Operation(
            summary = "Lista todos os funcionários",
            description = "Retorna a lista completa de funcionários cadastrados no sistema.",
            tags = "Administração"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @Operation(
            summary = "Busca funcionário por ID",
            description = "Retorna os dados de um funcionário específico pelo seu ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Funcionário encontrado"),
                    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado", content = @Content)
            },
            tags = "Administração"
    )
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getById(@PathVariable String id) {
        return employeeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Lista funcionários por empresa",
            description = "Retorna todos os funcionários vinculados a uma empresa específica.",
            tags = "Administração"
    )
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<EmployeeDto>> getByCompany(@PathVariable String companyId) {
        return ResponseEntity.ok(employeeService.findByCompanyId(companyId));
    }

    @Operation(
            summary = "Busca funcionário por e-mail",
            description = "Retorna os dados de um funcionário a partir do seu e-mail.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Funcionário encontrado"),
                    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado", content = @Content)
            },
            tags = "Administração"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/{email}")
    public ResponseEntity<EmployeeDto> getByEmail(@PathVariable String email) {
        return employeeService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Atualiza dados de um funcionário",
            description = "Atualiza as informações de um funcionário existente.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Funcionário atualizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado", content = @Content)
            },
            tags = "Administração"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> update(@PathVariable String id, @RequestBody EmployeeCreateDto dto) {
        return employeeService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Remove um funcionário",
            description = "Exclui um funcionário do sistema.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Funcionário removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado", content = @Content)
            },
            tags = "Administração"
    )
    @PreAuthorize("hasHole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }



    @Operation(
            summary = "Enviar respostas diárias (anônimas)",
            description = "Registra respostas anônimas e marca a participação diária do colaborador. "
                    + "Máximo 1 resposta por dia por survey por colaborador.",
            tags = "Funcionários"
    )
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/{employeeId}/{surveyId}/responses")
    public SurveyResponse submitDailySurveyResponse(
            @Parameter(example = "emp123") @PathVariable String employeeId,
            @Parameter(example = "surv789") @PathVariable String surveyId,
            @RequestBody SurveyResponseCreateDto dto
    ) {
        if (!employeeId.equals(dto.employeeId()) || !surveyId.equals(dto.surveyId())) {
            throw new RuntimeException("Dados inconsistentes na requisição");
        }
        return surveyResponseService.saveAnonymousDailyResponse(dto);
    }
}
