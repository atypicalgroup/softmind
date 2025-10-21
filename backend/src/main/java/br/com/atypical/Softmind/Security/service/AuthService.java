package br.com.atypical.Softmind.Security.service;

import br.com.atypical.Softmind.Security.dto.*;
import br.com.atypical.Softmind.Security.entities.PasswordResetToken;
import br.com.atypical.Softmind.Security.repository.PasswordResetRepository;
import br.com.atypical.Softmind.Security.helpers.EmailHelper;
import br.com.atypical.Softmind.Security.repository.UserRepository;
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

        // ✅ usa o novo método com template HTML
        emailHelper.sendPasswordResetEmail(email, token);

        return new PasswordResetResponseDto(
                "Código de verificação enviado com sucesso para o e-mail informado."
        );
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

        // ✅ Envia e-mail de confirmação
        emailHelper.sendPasswordChangedEmail(dto.email());

        return new PasswordResetResponseDto("Senha alterada com sucesso!");
    }

}
