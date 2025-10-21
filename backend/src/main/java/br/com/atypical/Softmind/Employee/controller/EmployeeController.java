package br.com.atypical.Softmind.Employee.controller;

import br.com.atypical.Softmind.Employee.dto.EmployeeCreateDto;
import br.com.atypical.Softmind.Employee.dto.EmployeeDto;
import br.com.atypical.Softmind.Employee.repository.EmployeeRepository;
import br.com.atypical.Softmind.Employee.service.EmployeeService;
import br.com.atypical.Softmind.Security.entities.User;
import br.com.atypical.Softmind.Security.service.UserService;
import br.com.atypical.Softmind.Survey.dto.SurveyResponseCreateDto;
import br.com.atypical.Softmind.Survey.entities.SurveyResponse;
import br.com.atypical.Softmind.Survey.service.SurveyResponseService;
import br.com.atypical.Softmind.shared.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;
    private final UserService userService;
    private final SurveyResponseService surveyResponseService;

    // ==========================================================
    // 🔹 CREATE EMPLOYEE (via UserService)
    // ==========================================================
    @Operation(
            summary = "Cria um novo funcionário",
            description = "Registra um funcionário vinculado à empresa do usuário logado e envia um e-mail de boas-vindas com senha temporária.",
            tags = "Administração"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(
            @RequestBody EmployeeCreateDto dto,
            @AuthenticationPrincipal User user
    ) {
        var creator = employeeRepository.findById(user.getEmployeeId())
                .orElseThrow(() -> new NotFoundException("Funcionário criador não encontrado"));

        var createdUser = userService.registerEmployee(dto, creator.getCompanyId());
        var employeeOpt = employeeService.findById(createdUser.getEmployeeId());

        return employeeOpt
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Falha ao criar funcionário"));
    }

    // ==========================================================
    // 🔹 READ OPERATIONS
    // ==========================================================
    @Operation(summary = "Lista todos os funcionários", tags = "Administração")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @Operation(summary = "Busca funcionário por ID", tags = "Administração")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getById(@PathVariable String id) {
        return employeeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Busca funcionário por e-mail", tags = "Administração")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/email/{email}")
    public ResponseEntity<EmployeeDto> getByEmail(@PathVariable String email) {
        return employeeService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ==========================================================
    // 🔹 UPDATE EMPLOYEE (via UserService)
    // ==========================================================
    @Operation(summary = "Atualiza dados de um funcionário", tags = "Administração")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(
            @PathVariable String id,
            @RequestBody EmployeeCreateDto dto,
            @AuthenticationPrincipal User user
    ) {
        var creator = employeeRepository.findById(user.getEmployeeId())
                .orElseThrow(() -> new NotFoundException("Funcionário autenticado não encontrado"));

        EmployeeDto updated = userService.updateEmployeeAndUser(id, dto, creator.getCompanyId());
        return ResponseEntity.ok(updated);
    }

    // ==========================================================
    // 🔹 DELETE
    // ==========================================================
    @Operation(summary = "Remove um funcionário", tags = "Administração")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ==========================================================
    // 🔹 SURVEY (funcionário)
    // ==========================================================
    @Operation(
            summary = "Enviar respostas diárias (anônimas)",
            description = "Registra respostas anônimas e marca a participação diária do colaborador. Máximo 1 resposta por dia por survey.",
            tags = "Funcionários"
    )
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/responses/daily")
    public ResponseEntity<SurveyResponse> submitDailySurveyResponse(
            @AuthenticationPrincipal User user,
            @RequestBody SurveyResponseCreateDto dto
    ) {
        SurveyResponse response = surveyResponseService.saveAnonymousDailyResponse(user, dto);
        return ResponseEntity.status(201).body(response);
    }
}
