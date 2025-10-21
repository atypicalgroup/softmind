package br.com.atypical.Softmind.security.service;

import br.com.atypical.Softmind.security.dto.*;
import br.com.atypical.Softmind.security.entities.PasswordResetToken;
import br.com.atypical.Softmind.security.repository.PasswordResetRepository;
import br.com.atypical.Softmind.security.helpers.EmailHelper;
import br.com.atypical.Softmind.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordResetRepository resetRepository;
    private final EmailHelper emailHelper;
    private final PasswordEncoder passwordEncoder;

    // 1️⃣ Envia token por e-mail
    public PasswordResetResponseDto sendResetToken(String email) {
        var user = userRepository.findByUsername(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        String token = String.format("%06d", new Random().nextInt(999999));

        PasswordResetToken reset = new PasswordResetToken();
        reset.setEmail(email);
        reset.setToken(token);
        reset.setExpiration(LocalDateTime.now().plusMinutes(15));
        reset.setUsed(false);

        resetRepository.save(reset);

        emailHelper.sendEmail(email, "Recuperação de Senha",
                "Olá, seu código de verificação é: " + token + "\n\n" +
                        "Este código expira em 15 minutos.");

        return new PasswordResetResponseDto("Código de verificação enviado com sucesso para o e-mail informado.");
    }

    // 2️⃣ Valida token
    public PasswordResetResponseDto verifyToken(PasswordVerifyDto dto) {
        var token = resetRepository.findByEmailAndTokenAndUsedFalse(dto.email(), dto.token())
                .orElseThrow(() -> new RuntimeException("Código inválido ou já utilizado."));

        if (token.getExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Código expirado.");
        }

        return new PasswordResetResponseDto("Código válido.");
    }

    // 3️⃣ Altera senha com token válido
    public PasswordResetResponseDto changePassword(PasswordChangeDto dto) {
        var token = resetRepository.findByEmailAndTokenAndUsedFalse(dto.email(), dto.token())
                .orElseThrow(() -> new RuntimeException("Código inválido ou já utilizado."));

        if (token.getExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Código expirado.");
        }

        var user = userRepository.findByUsername(dto.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        token.setUsed(true);

        resetRepository.save(token);
        userRepository.save(user);

        return new PasswordResetResponseDto("Senha alterada com sucesso!");
    }
}
