package br.com.atypical.Softmind.Security.controller;

import br.com.atypical.Softmind.Security.dto.*;
import br.com.atypical.Softmind.Security.entities.User;
import br.com.atypical.Softmind.Security.helpers.jwt.JwtService;
import br.com.atypical.Softmind.Security.service.AuthService;
import br.com.atypical.Softmind.Security.service.UserService;
import br.com.atypical.Softmind.Survey.service.SurveyResponseService;
import br.com.atypical.Softmind.shared.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final AuthService authService;
    private final SurveyResponseService surveyResponse;

    // ==========================================================
    // 🔐 LOGIN
    // ==========================================================
    @Operation(
            summary = "Login do usuário",
            description = "Autentica o usuário e retorna um JWT com informações adicionais (nome, role, companyId).",
            tags = {"Autenticação"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                            content = @Content(schema = @Schema(implementation = LoginResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody(description = "Credenciais do usuário", required = true)
            @org.springframework.web.bind.annotation.RequestBody LoginRequestDto loginRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    )
            );

            User user = userService.findByUsername(loginRequest.username())
                    .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

            if (user.isMustChangePassword()) {
                return ResponseEntity.status(403).body(
                        new LoginPendingChangeDto(
                                user.getId(),
                                user.getUsername(),
                                "É necessário alterar a senha no primeiro acesso."
                        )
                );
            }

            // ==========================================================
            // 🔹 Extrai dados do funcionário (se houver)
            // ==========================================================
            String employeeName = "Administrador";
            String companyId = null;

            if (user.getEmployeeId() != null) {
                var employee = userService.findEmployeeById(user.getEmployeeId()).orElse(null);
                if (employee != null) {
                    employeeName = employee.name();
                    companyId = employee.companyId();
                }
            }

            // ==========================================================
            // 🔹 Gera token com claims extras
            // ==========================================================
            String token = jwtService.generateTokenWithClaims(
                    loginRequest.username(),
                    Map.of(
                            "name", employeeName,
                            "role", user.getPermission(),
                            "companyId", companyId,
                            "employeeId", user.getEmployeeId()
                    )
            );

            // ==========================================================
            // 🔹 Checa se respondeu a pesquisa do dia
            // ==========================================================
            boolean alreadyAnswered = false;
            try {
                alreadyAnswered = surveyResponse.hasAnsweredToday(user);
            } catch (NotFoundException e) {
                // ignora se não houver pesquisa ativa
            }

            // ==========================================================
            // 🔹 Retorna o DTO completo
            // ==========================================================
            return ResponseEntity.ok(new LoginResponseDto(
                    token,
                    loginRequest.username(),
                    employeeName,
                    companyId,
                    alreadyAnswered
            ));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciais inválidas"));
        }
    }

    // ==========================================================
    // 🔹 Demais endpoints (recuperação e registro)
    // ==========================================================
    @PostMapping("/register/admin")
    public ResponseEntity<AdminResponseDto> registerAdmin(
            @RequestBody(description = "Dados do administrador", required = true)
            @org.springframework.web.bind.annotation.RequestBody AdminRegisterDto dto) {
        AdminResponseDto response = userService.registerAdmin(dto);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<PasswordResetResponseDto> forgotPassword(
            @RequestBody(description = "E-mail do usuário", required = true)
            @org.springframework.web.bind.annotation.RequestBody PasswordResetRequestDto dto) {
        return ResponseEntity.ok(authService.sendResetToken(dto.email()));
    }

    @PostMapping("/verify-token")
    public ResponseEntity<PasswordResetResponseDto> verifyToken(
            @org.springframework.web.bind.annotation.RequestBody PasswordVerifyDto dto) {
        return ResponseEntity.ok(authService.verifyToken(dto));
    }

    @PostMapping("/change-password")
    public ResponseEntity<PasswordResetResponseDto> changePassword(
            @org.springframework.web.bind.annotation.RequestBody PasswordChangeDto dto) {
        return ResponseEntity.ok(authService.changePassword(dto));
    }

    @PostMapping("/change-password-first-access")
    public ResponseEntity<Void> changePasswordFirstAccess(
            @org.springframework.web.bind.annotation.RequestBody ChangePasswordFirstAccessDto dto) {
        userService.changePassword(dto.userId(), dto.newPassword());
        return ResponseEntity.noContent().build();
    }
}
