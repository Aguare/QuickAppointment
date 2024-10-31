package com.example.app_backend.repositories;

import com.example.app_backend.entities.User;
import com.example.app_backend.entities.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserVerificationRepository extends JpaRepository<UserVerification, Integer> {

    UserVerification findByEmailAndToken(String email, String token);
    Optional<UserVerification> findByEmailTokenAndToken(String email, String token);

    UserVerification findByToken(String token);

    void deleteAllByEmail(String email);
    List<UserVerification> findByEmail(String fkUser);

    void deleteByToken(String token);
}
