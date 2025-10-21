package br.com.atypical.Softmind.Security.helpers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Component
@RequiredArgsConstructor
public class EmailHelper {

    private final JavaMailSender mailSender;

    public void sendWelcomeEmail(String to, String tempPassword) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            // 🔹 Carrega o template HTML do e-mail de boas-vindas
            String html;
            try (var inputStream = new ClassPathResource("templates/welcome-email.html").getInputStream()) {
                html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }

            // 🔹 Substitui variáveis dinâmicas do template
            html = html.replace("{{TEMP_PASSWORD}}", tempPassword);

            helper.setTo(to);
            helper.setSubject("🎉 Bem-vindo ao Softmind!");
            helper.setText(html, true);

            mailSender.send(message);
            System.out.println("📧 E-mail de boas-vindas enviado para: " + to);

        } catch (Exception e) {
            System.err.println("❌ Erro ao enviar e-mail de boas-vindas: " + e.getMessage());
        }
    }


    public void sendPasswordResetEmail(String to, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            // Carrega o template HTML
            String html = Files.readString(new ClassPathResource("templates/password-reset.html").getFile().toPath());
            html = html.replace("{{TOKEN}}", token);

            helper.setTo(to);
            helper.setSubject("Recuperação de Senha - Softmind");
            helper.setText(html, true);

            mailSender.send(message);
            System.out.println("📧 E-mail de recuperação enviado para: " + to);

        } catch (Exception e) {
            System.err.println("❌ Erro ao enviar e-mail: " + e.getMessage());
        }
    }

    public void sendPasswordChangedEmail(String to) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            String html;
            try (var inputStream = new ClassPathResource("templates/password-changed.html").getInputStream()) {
                html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }

            helper.setTo(to);
            helper.setSubject("Senha Alterada com Sucesso - Softmind");
            helper.setText(html, true);

            mailSender.send(message);
            System.out.println("📧 E-mail de confirmação enviado para: " + to);

        } catch (Exception e) {
            System.err.println("❌ Erro ao enviar e-mail de confirmação: " + e.getMessage());
        }
    }

}
