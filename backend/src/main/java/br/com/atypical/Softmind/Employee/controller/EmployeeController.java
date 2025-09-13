package br.com.atypical.Softmind.Employee.controller;

import br.com.atypical.Softmind.Employee.dto.EmployeeCreateDto;
import br.com.atypical.Softmind.Employee.dto.EmployeeDto;
import br.com.atypical.Softmind.Employee.service.EmployeeService;
import br.com.atypical.Softmind.Survey.dto.SurveyDto;
import br.com.atypical.Softmind.Survey.dto.SurveyResponseCreateDto;
import br.com.atypical.Softmind.Survey.entities.SurveyResponse;
import br.com.atypical.Softmind.Survey.service.SurveyResponseService;
import br.com.atypical.Softmind.Survey.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Tag(name = "3. Funcionários", description = "Gerenciamento de funcionários das empresas")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final SurveyService surveyService;
    private final SurveyResponseService surveyResponseService;

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
        return ResponseEntity.ok(employeeService.create(dto));
    }

    @Operation(
            summary = "Lista todos os funcionários",
            description = "Retorna a lista completa de funcionários cadastrados no sistema."
    )
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
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getById(@PathVariable String id) {
        return employeeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Lista funcionários por empresa",
            description = "Retorna todos os funcionários vinculados a uma empresa específica."
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
            }
    )
    @GetMapping("/email/{email}")
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
            }
    )
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
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Obter pesquisa diária para o colaborador",
            description = "Retorna sempre 10 perguntas: 2 fixas (Emoji do dia e Sentimento do dia) + 8 sorteadas da pool cadastrada pelo admin",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pesquisa retornada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Pesquisa não encontrada")
            }
    )
    @GetMapping("/{employeeId}/survey/{surveyId}")
    public SurveyDto getDailySurvey(
            @PathVariable String employeeId,
            @PathVariable String surveyId
    ) {
        // 🔹 aqui futuramente você pode validar se o employee pertence à empresa correta
        return surveyService.getSurveyForEmployee(surveyId);
    }

    @Operation(
            summary = "Enviar respostas diárias (anônimas)",
            description = "Registra respostas anônimas e marca a participação diária do colaborador. "
                    + "Máximo 1 resposta por dia por survey por colaborador."
    )
    @PostMapping("/{employeeId}/survey/{surveyId}/responses")
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

    @Operation(
            summary = "Contar respostas do colaborador",
            description = "Retorna quantos dias o colaborador já respondeu a pesquisa."
    )
    @GetMapping("/{employeeId}/survey/{surveyId}/responses/count")
    public Map<String, Object> getResponseCount(
            @Parameter(example = "emp123") @PathVariable String employeeId,
            @Parameter(example = "surv789") @PathVariable String surveyId
    ) {
        long count = surveyResponseService.countEmployeeResponses(employeeId, surveyId);
        return Map.of("employeeId", employeeId, "surveyId", surveyId, "daysResponded", count);
    }

}
