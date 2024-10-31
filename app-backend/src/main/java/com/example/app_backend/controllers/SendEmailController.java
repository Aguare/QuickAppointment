package com.example.app_backend.controllers;

import com.example.app_backend.entities.User;
import com.example.app_backend.entities.UserVerification;
import com.example.app_backend.helpers.EncryptUtil;
import com.example.app_backend.repositories.CompanySettingRepository;
import com.example.app_backend.repositories.UserRepository;
import com.example.app_backend.repositories.UserVerificationRepository;
import com.example.app_backend.services.SendEmailService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Service
public class SendEmailController {

    private UserRepository userRepository;
    private CompanySettingRepository companySettingRepository;
    private UserVerificationRepository userVerificationRepository;
    private final SendEmailService sendEmailService;

    @Transactional
    public void sendEmailVerification(String email) {
        String tokenGenerated = UUID.randomUUID().toString();
        String website = "http://localhost:4200/verify-email";

        User user = userRepository.findByEmailOrUsername(email, email);
        if (user == null) {
            throw new RuntimeException("El usuario no existe.");
        }
        email = user.getEmail();

        Map<String, String> dataCompany = new HashMap<>();
        companySettingRepository.findAllByKeyIn(
                List.of("company_name", "company_img", "password_salt")
        ).forEach(setting -> dataCompany.put(setting.getKey(), setting.getValue()));

        if (dataCompany.isEmpty()) {
            throw new RuntimeException("Error al obtener los datos de la empresa.");
        }

        String passwordSalt = dataCompany.get("password_salt");
        String tokenEncrypted = EncryptUtil.encrypt(tokenGenerated, passwordSalt);
        String emailEncrypted = EncryptUtil.encrypt(email, passwordSalt);

        LocalDateTime expiredAt = LocalDateTime.now().plusHours(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        UserVerification userSave = new UserVerification();
        userSave.setEmailToken(emailEncrypted);
        userSave.setEmail(email);
        userSave.setToken(tokenEncrypted);
        userSave.setVerification(true);
        userSave.setExpiredAt(expiredAt);

        userVerificationRepository.save(userSave);


        String companyName = dataCompany.get("company_name");

        String emailBody = "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f4f4f4; border-radius: 10px;\">" +
                "<h2 style=\"text-align: center; color: #333;\">¡Bienvenido a nuestro sitio!</h2>" +
                "<p style=\"color: #555; font-size: 16px;\">Gracias por registrarte. Para completar el proceso, por favor verifica tu correo electrónico haciendo clic en el botón de abajo.</p>" +
                "<div style=\"text-align: center; margin: 30px 0;\">" +
                "<a href=\"" + website + "/" + tokenEncrypted + "/" + emailEncrypted + "\" " +
                "style=\"display: inline-block; padding: 15px 25px; font-size: 18px; color: #fff; background-color: #28a745; text-decoration: none; border-radius: 5px;\">" +
                "Verificar correo electrónico</a></div>" +
                "<p style=\"color: #555; font-size: 14px;\">Si no solicitaste este correo, simplemente ignóralo.</p>" +
                "<hr style=\"border: none; border-top: 1px solid #ddd; margin: 20px 0;\">" +
                "<div style=\"text-align: center;\">" +
                "<img src=\"https://lh3.googleusercontent.com/a/ACg8ocLJq2dZdn1Py5pwjkNjI5G_OlenzSzDAOnxZ9B05WorrxO1Yx8=s576-c-no\" alt=\"Logo de la empresa\" style=\"max-width: 100px; margin-bottom: 10px;\">" +
                "</div>" +
                "<p style=\"color: #999; font-size: 12px; text-align: center;\">&copy; 2024 " + dataCompany.get("company_name") + ". Todos los derechos reservados.</p>" +
                "</div>";

        sendEmailService.sendEmail(email, "Verificación de Correo Electrónico", emailBody);
    }

    @Transactional
    public void sendRecoveryPasswordEmail(String email) {
        String tokenGenerated = UUID.randomUUID().toString();
        String website = "http://localhost:4200/reset-password";

        Map<String, String> dataCompany = new HashMap<>();
        companySettingRepository.findAllByKeyIn(
                List.of("company_name", "password_salt")
        ).forEach(setting -> dataCompany.put(setting.getKey(), setting.getValue()));

        if (dataCompany.isEmpty()) {
            throw new RuntimeException("Error al obtener los datos de la empresa.");
        }

        String passwordSalt = dataCompany.get("password_salt");
        String tokenEncrypted = EncryptUtil.encrypt(tokenGenerated, passwordSalt);
        String emailEncrypted = EncryptUtil.encrypt(email, passwordSalt);

        LocalDateTime expiredAt = LocalDateTime.now().plusHours(1);

        UserVerification userSave = new UserVerification();
        userSave.setEmailToken(emailEncrypted);
        userSave.setEmail(email);
        userSave.setToken(tokenEncrypted);
        userSave.setVerification(false);
        userSave.setExpiredAt(expiredAt);

        userVerificationRepository.save(userSave);

        String emailBody = "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f4f4f4; border-radius: 10px;\">" +
                "<h2 style=\"text-align: center; color: #333;\">Recuperación de Contraseña</h2>" +
                "<p style=\"color: #555; font-size: 16px;\">Hemos recibido una solicitud para recuperar tu contraseña. Haz clic en el botón de abajo para cambiar tu contraseña.</p>" +
                "<div style=\"text-align: center; margin: 30px 0;\">" +
                "<a href=\"" + website + "/" + tokenEncrypted + "/" + emailEncrypted + "\" " +
                "style=\"display: inline-block; padding: 15px 25px; font-size: 18px; color: #fff; background-color: #007bff; text-decoration: none; border-radius: 5px;\">" +
                "Recuperar Contraseña</a></div>" +
                "<p style=\"color: #555; font-size: 14px;\">Si no solicitaste este correo, simplemente ignóralo.</p>" +
                "<hr style=\"border: none; border-top: 1px solid #ddd; margin: 20px 0;\">" +
                "<div style=\"text-align: center;\">" +
                "<img src=\"https://lh3.googleusercontent.com/a/ACg8ocLJq2dZdn1Py5pwjkNjI5G_OlenzSzDAOnxZ9B05WorrxO1Yx8=s576-c-no\" alt=\"Logo de la empresa\" style=\"max-width: 100px; margin-bottom: 10px;\">" +
                "</div>" +
                "<p style=\"color: #999; font-size: 12px; text-align: center;\">&copy; 2024 " + dataCompany.get("company_name") + ". Todos los derechos reservados.</p>" +
                "</div>";

        sendEmailService.sendEmail(email, "Recuperación de Contraseña", emailBody);
    }
}