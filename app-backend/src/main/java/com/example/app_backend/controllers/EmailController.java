package com.example.app_backend.controllers;

import com.example.app_backend.repositories.UserRepository;
import com.example.app_backend.services.SendEmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Properties;

@AllArgsConstructor
@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private UserRepository userRepository;
    public static final String WEBSITE = "https://www.ejemplo.com";
    public static final String TOKEN_GENERATED = "tokenGenerado123";
    public static final String EMAIL_ENCRYPT = "emailEncriptado123";
    public static final String COMPANY_LOGO_URL = "https://lh3.googleusercontent.com/a/ACg8ocLJq2dZdn1Py5pwjkNjI5G_OlenzSzDAOnxZ9B05WorrxO1Yx8=s576-c-no";
    public static final String COMPANY_NAME = "NombreDeLaEmpresa";

    private String getEmailTemplate() {
        return "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f4f4f4; border-radius: 10px;\">" +
                "<h2 style=\"text-align: center; color: #333;\">¡Bienvenido a nuestro sitio!</h2>" +
                "<p style=\"color: #555; font-size: 16px;\">Gracias por registrarte. Para completar el proceso, por favor verifica tu correo electrónico haciendo clic en el botón de abajo.</p>" +

                "<div style=\"text-align: center; margin: 30px 0;\">" +
                "<a href=\"" + WEBSITE + "/" + TOKEN_GENERATED + "/" + EMAIL_ENCRYPT + "\" " +
                "style=\"display: inline-block; padding: 15px 25px; font-size: 18px; color: #fff; background-color: #28a745; text-decoration: none; border-radius: 5px;\">" +
                "Verificar correo electrónico" +
                "</a>" +
                "</div>" +

                "<p style=\"color: #555; font-size: 14px;\">Si no solicitaste este correo, simplemente ignóralo.</p>" +

                "<hr style=\"border: none; border-top: 1px solid #ddd; margin: 20px 0;\">" +

                "<div style=\"text-align: center;\">" +
                "<img src=\"" + COMPANY_LOGO_URL + "\" alt=\"Logo de la empresa\" style=\"max-width: 100px; margin-bottom: 10px;\">" +
                "</div>" +

                "<p style=\"color: #999; font-size: 12px; text-align: center;\">&copy; 2024 " + COMPANY_NAME + ". Todos los derechos reservados.</p>" +
                "</div>";
    }

    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    private final SendEmailService sendEmailService;

    @PostMapping("/send")
    ResponseEntity<String> sendEmail(@RequestBody Map<String, String> request) {
        try {
            String response = sendEmailService.sendEmail(request.get("email"), getEmailTemplate());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al enviar el email a {}: {}", request.get("email"), e.getMessage());
            return ResponseEntity.badRequest().body("Error al enviar el email: " + e.getMessage());
        }
    }
}
