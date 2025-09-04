package br.com.atypical.Softmind.security.controller;

import br.com.atypical.Softmind.security.entities.User;
import br.com.atypical.Softmind.security.helpers.jwt.JwtService;
import br.com.atypical.Softmind.security.repository.UserRepository;
import br.com.atypical.Softmind.security.service.UserService;
import br.com.atypical.Softmind.shared.enums.Role;
import br.com.atypical.Softmind.shared.exceptions.BadRequestException;
import br.com.atypical.Softmind.shared.exceptions.NotFoundException;
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
