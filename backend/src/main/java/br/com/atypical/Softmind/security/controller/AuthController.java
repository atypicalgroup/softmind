package br.com.atypical.Softmind.security.controller;

import br.com.atypical.Softmind.security.dto.AdminRegisterDto;
import br.com.atypical.Softmind.security.dto.AdminResponseDto;
import br.com.atypical.Softmind.security.dto.LoginRequestDto;
import br.com.atypical.Softmind.security.dto.LoginResponseDto;
import br.com.atypical.Softmind.security.entities.User;
import br.com.atypical.Softmind.security.helpers.jwt.JwtService;
import br.com.atypical.Softmind.security.service.UserService;
import br.com.atypical.Softmind.shared.enums.Permission;
import br.com.atypical.Softmind.shared.exceptions.BadRequestException;
import br.com.atypical.Softmind.shared.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Operation(
            summary = "Realiza login do usuário",
            description = "Autentica o usuário e retorna um JWT para uso nas próximas requisições",
            tags = "Login"
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    )
            );
            String token = jwtService.generateToken(loginRequest.username());
            return ResponseEntity.ok(
                    new LoginResponseDto(token, loginRequest.username())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).build();
        }
    }

    @Operation(
            summary = "Obtém dados do usuário autenticado",
            description = "Retorna informações básicas do usuário atual (via token JWT).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dados do usuário retornados"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
            },
            tags = "Funcionários"
    )
    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal User userDetails) {
        return userService.findByUsername(userDetails.getUsername())
                .map(user -> ResponseEntity.ok(Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "role", user.getPermission(),
                        "companyId", user.getCompanyId(),
                        "employeeId", user.getEmployeeId(),
                        "mustChangePassword", user.isMustChangePassword()
                )))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Usuário não encontrado")));
    }

    @Operation(
            summary = "Registra um novo administrador",
            description = "Cria um usuário com role ADMIN vinculado a uma empresa",
            tags = "Autenticação"
    )
    @PostMapping("/register/admin")
    public ResponseEntity<AdminResponseDto> registerAdmin(@RequestBody AdminRegisterDto dto) {
        User user = userService.register(dto.username(), dto.password(), null, null, Permission.ADMIN);

        return ResponseEntity.ok(
                new AdminResponseDto(user.getId(), user.getUsername(), user.getPermission())
        );
    }

    @Operation(
            summary = "Redefine a senha",
            description = "Permite que o usuário altere a senha (deve informar senha atual e nova senha)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Senha redefinida com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro de validação", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
            },
            tags = "Autenticação"
    )
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException("Senha atual incorreta");
        }

        if (newPassword.length() < 8) {
            throw new BadRequestException("A nova senha deve ter pelo menos 8 caracteres");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setMustChangePassword(false);
        userService.save(user);

        return ResponseEntity.ok(Map.of("message", "Senha redefinida com sucesso"));
    }
}
