package br.com.atypical.Softmind.Survey.controller;

import br.com.atypical.Softmind.Survey.dto.SurveyCreateDto;
import br.com.atypical.Softmind.Survey.dto.SurveyDto;
import br.com.atypical.Softmind.Survey.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
@Tag(name = "Pesquisa", description = "Gerenciamento de Pesquisas (Surveys)")
public class SurveyController {

    private final SurveyService service;

    @Operation(
            summary = "Cria uma nova pesquisa",
            description = "Cria uma nova pesquisa vinculada a uma empresa",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pesquisa criada com sucesso",
                            content = @Content(schema = @Schema(implementation = SurveyDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public SurveyDto create(@RequestBody SurveyCreateDto dto) {
        return service.createSurvey(dto);
    }

    @Operation(
            summary = "Lista todas as pesquisas de uma empresa",
            description = "Recupera todas as pesquisas vinculadas ao ID de uma empresa",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                            content = @Content(schema = @Schema(implementation = SurveyDto.class))),
                    @ApiResponse(responseCode = "404", description = "Empresa não encontrada", content = @Content)
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{companyId}")
    public List<SurveyDto> getByCompany(
            @Parameter(description = "ID da empresa", example = "66dff9b2c0d1a45a6e2b1234")
            @PathVariable String companyId) {
        return service.getByCompany(companyId);
    }

    @Operation(
            summary = "Busca uma pesquisa pelo ID",
            description = "Recupera os detalhes de uma pesquisa específica",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pesquisa encontrada",
                            content = @Content(schema = @Schema(implementation = SurveyDto.class))),
                    @ApiResponse(responseCode = "404", description = "Pesquisa não encontrada", content = @Content)
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public SurveyDto getById(
            @Parameter(description = "ID da pesquisa", example = "66dffa10c0d1a45a6e2b5678")
            @PathVariable String id) {
        return service.getById(id);
    }

    @Operation(
            summary = "Pesquisa diária para o colaborador",
            description = "Retorna sempre 10 perguntas: 2 fixas (Emoji do dia e Sentimento do dia) + 8 sorteadas da pool cadastrada pelo admin",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pesquisa retornada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Pesquisa não encontrada")
            },
            tags = "Funcionários"
    )
    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/{employeeId}/{surveyId}")
    public SurveyDto getDailySurvey(
            @PathVariable String employeeId,
            @PathVariable String surveyId
    ) {
        return service.getSurveyForEmployee(surveyId);
    }
}
