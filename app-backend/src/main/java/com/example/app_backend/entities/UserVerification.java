package com.example.app_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_verification")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email_token", nullable = false)
    private String emailToken;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "is_verification", nullable = false)
    private boolean isVerification;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;
}
