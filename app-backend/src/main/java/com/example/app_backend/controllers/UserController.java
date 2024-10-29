package com.example.app_backend.controllers;

import com.example.app_backend.dtos.LoginDto;
import com.example.app_backend.dtos.PageInfoDto;
import com.example.app_backend.dtos.UserDto;
import com.example.app_backend.entities.User;
import com.example.app_backend.entities.UserHasRole;
import com.example.app_backend.helpers.ApiResponse;
import com.example.app_backend.helpers.LoginResponse;
import com.example.app_backend.repositories.UserHasRoleRepository;
import com.example.app_backend.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        UserHasRole userHasRole = new UserHasRole();
        userHasRole.setFkUser(savedUser.getId());
        userHasRole.setFkRole(2);
        userHasRoleRepository.save(userHasRole);

        // success message
        ApiResponse response = new ApiResponse("Usuario creado con éxito.", savedUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginDto loginDto) {
        User user = null;

        if (loginDto.getUsername().contains("@")) {

            user = userRepository.findByEmail(loginDto.getUsername());
            if (user == null) {
                LoginResponse response = new LoginResponse("El email no existe.", null, null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } else {
            user = userRepository.findByUsername(loginDto.getUsername());
            if (user == null) {
                LoginResponse response = new LoginResponse("El nombre de usuario no existe.",null, null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        }

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            LoginResponse response = new LoginResponse("La contraseña es incorrecta.",null, null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        UserHasRole userRole = userHasRoleRepository.findByFkUser(user.getId());
        if (userRole == null) {
            LoginResponse response = new LoginResponse("No se encontró un rol asociado al usuario.",null, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        LoginResponse response = new LoginResponse("Login exitoso", user.getId(), userRole.getFkRole());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pages/{userId}")
    public ResponseEntity<List<PageInfoDto>> getPagesByUserId(@PathVariable("userId") Integer userId) {
        List<Object[]> results = userHasRoleRepository.findPageInfoByUserId(userId);

        List<PageInfoDto> pageInfoList = results.stream()
                .map(result -> new PageInfoDto(
                        (Integer) result[0],         // id
                        (String) result[1],          // pageName
                        (String) result[2],          // direction
                        (Boolean) result[3],         // isAvailable
                        (String) result[4]           // moduleName
                ))
                .collect(Collectors.toList());

        if (pageInfoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(pageInfoList);
    }
}

