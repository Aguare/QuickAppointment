package com.example.app_backend.controllers;

import com.example.app_backend.entities.User;
import com.example.app_backend.entities.UserVerification;
import com.example.app_backend.repositories.UserRepository;
import com.example.app_backend.repositories.UserVerificationRepository;
import com.example.app_backend.services.SendEmailService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
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
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserVerificationRepository userVerificationRepository;

    private SendEmailController sendEmailController;

    @Transactional
    @PostMapping("/verify-email")
    public ResponseEntity<String> verificationEmail(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        String token = request.get("token");

        if (email == null || token == null) {
            return ResponseEntity.badRequest().body("Faltan parámetros: 'email' o 'token'.");
        }

        Optional<UserVerification> userVerification = userVerificationRepository.findByEmailTokenAndToken(email, token);
        if (userVerification.isEmpty()) {
            return ResponseEntity.badRequest().body("Token no válido.");
        }

        UserVerification userVerificationData = userVerification.get();

        userVerificationRepository.deleteAllByEmail(userVerificationData.getEmail());

        User user = userRepository.findByEmail(userVerificationData.getEmail());
        if (user == null) {
            return ResponseEntity.badRequest().body("Usuario no encontrado.");
        }

        user.setIdVerified(true);
        userRepository.save(user);

        return ResponseEntity.ok("¡Email verificado correctamente!");
    }

    @PostMapping("/sendVerificationEmail")
    public ResponseEntity<String> sendEmailVerification(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (email == null) {
            return ResponseEntity.badRequest().body("Falta el parámetro 'email'.");
        }

        User user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.badRequest().body("El usuario no existe.");
        }

        sendEmailController.sendEmailVerification(email);

        return ResponseEntity.ok("{ message: \"¡Email de verificación enviado correctamente!\"}");
    }

    @PostMapping("/validateEmail")
    public ResponseEntity<String> validateEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (email == null) {
            return ResponseEntity.badRequest().body("Falta el parámetro 'email'.");
        }

        User user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.badRequest().body("El usuario no existe.");
        }

        return ResponseEntity.ok("{ message: \"¡Email válido!\", success: true }");
    }

    @PostMapping("/sendRecoveryPasswordEmail")
    public ResponseEntity<String> sendRecoveryPasswordEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (email == null) {
            return ResponseEntity.badRequest().body("Falta el parámetro 'email'.");
        }

        User user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.badRequest().body("El usuario no existe.");
        }

        sendEmailController.sendRecoveryPasswordEmail(email);

        return ResponseEntity.ok("{ message: \"¡Email de recuperación de contraseña enviado correctamente!\"}");
    }

}
