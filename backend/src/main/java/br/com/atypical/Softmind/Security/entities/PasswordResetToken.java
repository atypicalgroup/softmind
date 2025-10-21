package br.com.atypical.Softmind.Security.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "tb_password_reset_tokens")
public class PasswordResetToken {

    @Id
    private String id;

    private String email;
    private String token; // 6 dígitos
    private LocalDateTime expiration;
    private boolean used;
}
