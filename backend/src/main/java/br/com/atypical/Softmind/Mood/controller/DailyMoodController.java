package br.com.atypical.Softmind.Mood.controller;

import br.com.atypical.Softmind.Employee.entities.Employee;
import br.com.atypical.Softmind.Employee.repository.EmployeeRepository;
import br.com.atypical.Softmind.Mood.dto.DailyMoodRequestDto;
import br.com.atypical.Softmind.Mood.service.DailyMoodService;
import br.com.atypical.Softmind.Security.entities.User;
import br.com.atypical.Softmind.shared.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/mood")
public class DailyMoodController {

    private final DailyMoodService dailyMoodService;
    private final EmployeeRepository employeeRepository;

    @Operation(
            summary = "Registra humor diário e retorna recomendações de filmes",
            description = "O companyId e employeeId são preenchidos automaticamente do usuário autenticado",
            tags = "Funcionários"
    )
    @PostMapping("/daily/recommendations")
    public ResponseEntity<?> saveMoodAndGetRecommendations(
            @AuthenticationPrincipal User user,
            @RequestBody DailyMoodRequestDto dto
    ) {

        Employee creator = employeeRepository.findById(user.getEmployeeId())
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));

        return ResponseEntity.ok(dailyMoodService.saveAndRecommend(
                creator.getCompanyId(),
                user.getEmployeeId(),
                dto
        ));
    }
}
