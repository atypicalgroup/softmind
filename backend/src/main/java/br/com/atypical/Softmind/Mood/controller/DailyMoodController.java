package br.com.atypical.Softmind.Mood.controller;

import br.com.atypical.Softmind.Employee.entities.Employee;
import br.com.atypical.Softmind.Employee.repository.EmployeeRepository;
import br.com.atypical.Softmind.Mood.dto.DailyMoodRequestDto;
import br.com.atypical.Softmind.Mood.dto.DailyMoodResponseDto;
import br.com.atypical.Softmind.Movie.dto.MovieDto;
import br.com.atypical.Softmind.Survey.dto.QuestionDto;
import br.com.atypical.Softmind.Mood.service.DailyMoodService;
import br.com.atypical.Softmind.security.entities.User;
import br.com.atypical.Softmind.shared.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/mood")
@Tag(name = "Daily Mood", description = "Endpoints para humor diário")
public class DailyMoodController {

    private final DailyMoodService dailyMoodService;
    private final EmployeeRepository employeeRepository;

    @Operation(
            summary = "Registra humor diário e retorna recomendações de filmes",
            description = "O companyId e employeeId são preenchidos automaticamente do usuário autenticado"
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
