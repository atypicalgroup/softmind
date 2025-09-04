package br.com.atypical.Softmind.security.controller;

import br.com.atypical.Softmind.security.entities.User;
import br.com.atypical.Softmind.security.helpers.jwt.JwtService;
import br.com.atypical.Softmind.security.service.UserService;
import br.com.atypical.Softmind.shared.enums.Role;
import br.com.atypical.Softmind.shared.exceptions.BadRequestException;
import br.com.atypical.Softmind.shared.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/auth")
@Tag(name = "1. Autenticação", description = "Endpoints para login, registro e gerenciamento de usuários")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Operation(
            summary = "Realiza login do usuário",
            description = "Autentica o usuário e retorna um JWT para uso nas próximas requisições",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content)
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = jwtService.generateToken(username);

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "username", username
            ));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciais inválidas"));
        }
    }

    @Operation(
            summary = "Obtém dados do usuário autenticado",
            description = "Retorna informações básicas do usuário atual (via token JWT).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dados do usuário retornados"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
            }
    )
    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal User userDetails) {
        return userService.findByUsername(userDetails.getUsername())
                .map(user -> ResponseEntity.ok(Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "role", user.getRole(),
                        "companyId", user.getCompanyId(),
                        "employeeId", user.getEmployeeId(),
                        "mustChangePassword", user.isMustChangePassword()
                )))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Usuário não encontrado")));
    }

    @Operation(
            summary = "Registra um novo administrador",
            description = "Cria um usuário com role ADMIN vinculado a uma empresa"
    )
    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String companyId = request.get("companyId");

        User user = userService.register(username, password, companyId, null, Role.ADMIN);
        return ResponseEntity.ok(Map.of(
                "message", "Usuário ADMIN registrado com sucesso",
                "username", user.getUsername(),
                "role", user.getRole()
        ));
    }

    @Operation(
            summary = "Registra um novo funcionário",
            description = "Cria um usuário com role EMPLOYEE vinculado a um funcionário e a uma empresa"
    )
    @PostMapping("/register/employee")
    public ResponseEntity<?> registerEmployee(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password"); // normalmente genérica
        String companyId = request.get("companyId");
        String employeeId = request.get("employeeId");

        User user = userService.registerEmployee(username, password, companyId, employeeId);
        return ResponseEntity.ok(Map.of(
                "message", "Usuário EMPLOYEE registrado com sucesso",
                "username", user.getUsername(),
                "role", user.getRole(),
                "mustChangePassword", user.isMustChangePassword()
        ));
    }

    @Operation(
            summary = "Redefine a senha",
            description = "Permite que o usuário altere a senha (deve informar senha atual e nova senha)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Senha redefinida com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro de validação", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
            }
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
