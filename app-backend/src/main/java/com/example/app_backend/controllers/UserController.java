package com.example.app_backend.controllers;

import com.example.app_backend.dtos.LoginDto;
import com.example.app_backend.dtos.UserDto;
import com.example.app_backend.entities.User;
import com.example.app_backend.entities.UserHasRole;
import com.example.app_backend.helpers.ApiResponse;
import com.example.app_backend.repositories.UserHasRoleRepository;
import com.example.app_backend.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserHasRoleRepository userHasRoleRepository;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody UserDto userDto) {

        if (userRepository.existsByEmail(userDto.getEmail())) {
            ApiResponse response = new ApiResponse("El email ya está en uso.", null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        if (userRepository.existsByUsername(userDto.getUsername())) {
            ApiResponse response = new ApiResponse("El nombre de usuario ya está en uso.", null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        // create user
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());

        // encrypt password
        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(encryptedPassword);

        // save user
        User savedUser = userRepository.save(user);

        // Add user has role
        UserHasRole userHasRole = new UserHasRole(savedUser.getId(), 2); // Rol 2
        userHasRoleRepository.save(userHasRole);

        // success message
        ApiResponse response = new ApiResponse("Usuario creado con éxito.", savedUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody LoginDto loginDto) {
        User user = null;

        if (loginDto.getUsername().contains("@")) {

            user = userRepository.findByEmail(loginDto.getUsername());
            if (user == null) {
                ApiResponse response = new ApiResponse("El email no existe.", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } else {
            user = userRepository.findByUsername(loginDto.getUsername());
            if (user == null) {
                ApiResponse response = new ApiResponse("El nombre de usuario no existe.", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        }

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            ApiResponse response = new ApiResponse("La contraseña es incorrecta.", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        ApiResponse response = new ApiResponse("Login exitoso.", user.getId());
        return ResponseEntity.ok(response);
    }
}

