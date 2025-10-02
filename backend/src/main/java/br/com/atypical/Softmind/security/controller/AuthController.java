package br.com.atypical.Softmind.security.controller;

import br.com.atypical.Softmind.security.dto.AdminRegisterDto;
import br.com.atypical.Softmind.security.dto.AdminResponseDto;
import br.com.atypical.Softmind.security.dto.LoginRequestDto;
import br.com.atypical.Softmind.security.dto.LoginResponseDto;
import br.com.atypical.Softmind.security.entities.User;
import br.com.atypical.Softmind.security.helpers.jwt.JwtService;
import br.com.atypical.Softmind.security.service.UserService;
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
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Operation(
            summary = "Login do usuário",
            description = "Autentica o usuário com e-mail e senha e retorna um JWT. O token contém também o nome do funcionário vinculado.",
            tags = {"Login"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciais de acesso",
                    required = true
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso. Retorna o JWT e dados do usuário"),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
            }
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

            // Busca usuário e employee vinculado
            User user = userService.findByUsername(loginRequest.username())
                    .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

            String employeeName = "N/A";
            if (user.getEmployeeId() != null) {
                employeeName = userService.findEmployeeNameById(user.getEmployeeId())
                        .orElse("N/A"); // você cria esse método no service para buscar o nome
            }

            // Cria token com claims extras
            String token = jwtService.generateTokenWithClaims(
                    loginRequest.username(),
                    Map.of("name", employeeName)
            );

            return ResponseEntity.ok(
                    new LoginResponseDto(token, loginRequest.username(), employeeName)
            );

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).build();
        }
    }


    @Operation(
            summary = "Registra um novo administrador",
            description = "Cria um usuário com role ADMIN vinculado a uma empresa",
            tags = "Autenticação"
    )
    @PostMapping("/register/admin")
    public ResponseEntity<AdminResponseDto> registerAdmin(@RequestBody AdminRegisterDto dto) {
        AdminResponseDto response = userService.registerAdmin(dto);
        return ResponseEntity.ok(response);
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
