package br.com.atypical.Softmind;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordCheckRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        var encoder = new BCryptPasswordEncoder();
        String raw = "Admin123"; // senha que você está usando no login
        String encoded = "$2a$10$pIeKwCJyJ34dQhXaZ9T4zOkZb11JcL.bmtRlUTBcfDS9KwldkgGgm"; // hash do banco

        boolean match = encoder.matches(raw, encoded);
        System.out.println("🔍 Senha válida? " + match);
    }
}
