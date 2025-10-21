package br.com.atypical.Softmind.Security.helpers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import jakarta.mail.internet.MimeMessage;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class EmailHelper {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;
    private static final String FROM_NAME = "Softmind Suporte";

    public void sendWelcomeEmail(String to, String tempPassword) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            // 🔹 Carrega o template HTML
            String html;
            try (var inputStream = new ClassPathResource("templates/welcome-email.html").getInputStream()) {
                html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }

            // 🔹 Substitui variáveis
            html = html.replace("{{TEMP_PASSWORD}}", tempPassword);

            // ✅ Define remetente fixo e confiável
            helper.setFrom(fromEmail, FROM_NAME);
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

            String html;
            try (var inputStream = new ClassPathResource("templates/password-reset.html").getInputStream()) {
                html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }

            html = html.replace("{{TOKEN}}", token);

            helper.setFrom(fromEmail, FROM_NAME);
            helper.setTo(to);
            helper.setSubject("🔐 Recuperação de Senha - Softmind");
            helper.setText(html, true);

            mailSender.send(message);
            System.out.println("📧 E-mail de recuperação enviado para: " + to);

        } catch (Exception e) {
            System.err.println("❌ Erro ao enviar e-mail de recuperação: " + e.getMessage());
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

            helper.setFrom(fromEmail, FROM_NAME);
            helper.setTo(to);
            helper.setSubject("✅ Senha Alterada com Sucesso - Softmind");
            helper.setText(html, true);

            mailSender.send(message);
            System.out.println("📧 E-mail de confirmação enviado para: " + to);

        } catch (Exception e) {
            System.err.println("❌ Erro ao enviar e-mail de confirmação: " + e.getMessage());
        }
    }
}
