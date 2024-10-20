package com.example.app_backend.controllers;

import com.example.app_backend.dtos.LoginDto;
import com.example.app_backend.dtos.UserDto;
import com.example.app_backend.entities.User;
import com.example.app_backend.helpers.ApiResponse;
import com.example.app_backend.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

        // Crear el objeto User
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());

        // Encriptar la contraseña con PBKDF2
        System.out.println(userDto.getPassword());
        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(encryptedPassword);

        // Guardar en la base de datos
        User savedUser = userRepository.save(user);

        // Devolver mensaje de éxito
        ApiResponse response = new ApiResponse("Usuario creado con éxito.", savedUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody LoginDto loginDto) {
        User user = null;

        // Buscar por email o username
        if (loginDto.getUsername().contains("@")) {
            // Buscar por email
            user = userRepository.findByEmail(loginDto.getUsername());
            if (user == null) {
                ApiResponse response = new ApiResponse("El email no existe.", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } else {
            // Buscar por username
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

        // Devolver respuesta de éxito
        ApiResponse response = new ApiResponse("Login exitoso.", user.getId());
        return ResponseEntity.ok(response);
    }
}

