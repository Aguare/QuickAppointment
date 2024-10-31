package com.example.app_backend.controllers;

import com.example.app_backend.entities.CompanySetting;
import com.example.app_backend.repositories.CompanyRepository;
import com.example.app_backend.repositories.CompanySettingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class MailConfig {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.protocol}")
    private String protocol;

    private final CompanySettingRepository companySettingRepository;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setProtocol(protocol);

        Optional<CompanySetting> gmailAddress = this.companySettingRepository.findByKey("gmail_address");
        Optional<CompanySetting> gmailPassword = this.companySettingRepository.findByKey("gmail_password");

        // Verificar si las credenciales están presentes
        if (gmailAddress.isEmpty() || gmailPassword.isEmpty()) {
            throw new RuntimeException("No se encontraron las credenciales del correo (gmail_address o gmail_password ausentes en la configuración).");
        }

        // Asignar las credenciales obtenidas de la base de datos
        javaMailSender.setUsername(gmailAddress.get().getValue());
        javaMailSender.setPassword(gmailPassword.get().getValue());

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.starttls.required", true);

        return javaMailSender;
    }

}
