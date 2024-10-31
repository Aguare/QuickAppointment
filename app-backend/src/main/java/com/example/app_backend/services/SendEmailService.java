package com.example.app_backend.services;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SendEmailService {

    private final JavaMailSender mailSender;

    public String sendEmail(String email, String subject, String message) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(message, true);

            mailSender.send(mimeMessage);
            return "El correo se envió exitosamente!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
