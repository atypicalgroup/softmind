package br.com.atypical.Softmind.security.controller;

import br.com.atypical.Softmind.security.dto.*;
import br.com.atypical.Softmind.security.entities.User;
import br.com.atypical.Softmind.security.helpers.jwt.JwtService;
import br.com.atypical.Softmind.security.service.AuthService;
import br.com.atypical.Softmind.security.service.UserService;
import br.com.atypical.Softmind.shared.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints de autenticação, recuperação de senha e registro de usuários")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    // 🔹 LOGIN ---------------------------------------------------------
    @Operation(
            summary = "Login do usuário",
            description = "Autentica o usuário com e-mail e senha e retorna um JWT. O token contém também o nome do funcionário vinculado.",
            tags = {"Autenticação"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                            content = @Content(schema = @Schema(implementation = LoginResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
            }
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
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
                    .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

            String employeeName = user.getEmployeeId() != null
                    ? userService.findEmployeeNameById(user.getEmployeeId()).orElse("N/A")
                    : "N/A";

            String token = jwtService.generateTokenWithClaims(
                    loginRequest.username(),
                    Map.of("name", employeeName)
            );

            return ResponseEntity.ok(new LoginResponseDto(token, loginRequest.username(), employeeName));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).build();
        }
    }

    // 🔹 REGISTRO DE ADMIN --------------------------------------------
    @Operation(
            summary = "Registra um novo administrador",
            description = "Cria um novo usuário com perfil ADMIN vinculado a uma empresa.",
            tags = {"Autenticação"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Administrador criado com sucesso",
                            content = @Content(schema = @Schema(implementation = AdminResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
            }
    )
    @PostMapping("/register/admin")
    public ResponseEntity<AdminResponseDto> registerAdmin(
            @RequestBody(description = "Dados do administrador", required = true)
            @org.springframework.web.bind.annotation.RequestBody AdminRegisterDto dto) {
        return ResponseEntity.ok(userService.registerAdmin(dto));
    }

    // 🔹 ESQUECI MINHA SENHA ------------------------------------------
    @Operation(
            summary = "Solicitar recuperação de senha",
            description = "Envia um e-mail com um token de 6 dígitos para redefinição de senha. O código expira em 15 minutos.",
            tags = {"Recuperação de Senha"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Código de verificação enviado",
                            content = @Content(schema = @Schema(implementation = PasswordResetResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
            }
    )
    @PostMapping("/forgot-password")
    public ResponseEntity<PasswordResetResponseDto> forgotPassword(
            @RequestBody(description = "E-mail do usuário para envio do token", required = true)
            @org.springframework.web.bind.annotation.RequestBody PasswordResetRequestDto dto) {
        return ResponseEntity.ok(authService.sendResetToken(dto.email()));
    }

    // 🔹 VALIDAR TOKEN -------------------------------------------------
    @Operation(
            summary = "Validar token de redefinição de senha",
            description = "Valida se o token informado é válido, pertence ao e-mail e ainda não expirou.",
            tags = {"Recuperação de Senha"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token válido",
                            content = @Content(schema = @Schema(implementation = PasswordResetResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Token inválido ou expirado", content = @Content)
            }
    )
    @PostMapping("/verify-token")
    public ResponseEntity<PasswordResetResponseDto> verifyToken(
            @RequestBody(description = "E-mail e token a validar", required = true)
            @org.springframework.web.bind.annotation.RequestBody PasswordVerifyDto dto) {
        return ResponseEntity.ok(authService.verifyToken(dto));
    }

    // 🔹 ALTERAR SENHA ------------------------------------------------
    @Operation(
            summary = "Alterar senha com token válido",
            description = "Permite alterar a senha informando o token de verificação recebido por e-mail.",
            tags = {"Recuperação de Senha"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso",
                            content = @Content(schema = @Schema(implementation = PasswordResetResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Token inválido ou expirado", content = @Content)
            }
    )
    @PostMapping("/change-password")
    public ResponseEntity<PasswordResetResponseDto> changePassword(
            @RequestBody(description = "E-mail, token e nova senha", required = true)
            @org.springframework.web.bind.annotation.RequestBody PasswordChangeDto dto) {
        return ResponseEntity.ok(authService.changePassword(dto));
    }
}
