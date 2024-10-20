package com.example.app_backend.controllers;


import com.example.app_backend.dtos.LoginDto;
import com.example.app_backend.dtos.UserDto;
import com.example.app_backend.entities.User;
import com.example.app_backend.helpers.ApiResponse;
import com.example.app_backend.repositories.UserHasRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private com.example.app_backend.repositories.UserRepository userRepository;

    @Mock
    private UserHasRoleRepository userHasRoleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setUsername("testuser");
        userDto.setPassword("password123");

        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

        // Act
        ResponseEntity<ApiResponse> response = userController.registerUser(userDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("El email ya está en uso.", response.getBody().getMessage());
        verify(userRepository, times(1)).existsByEmail(userDto.getEmail());
        verify(userRepository, never()).save(any(com.example.app_backend.entities.User.class));
    }

    @Test
    void testRegisterUser_UsernameAlreadyExists() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setUsername("testuser");
        userDto.setPassword("password123");

        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(true);

        // Act
        ResponseEntity<ApiResponse> response = userController.registerUser(userDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("El nombre de usuario ya está en uso.", response.getBody().getMessage());
        verify(userRepository, times(1)).existsByUsername(userDto.getUsername());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterUser_SuccessfulRegistration() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setUsername("testuser");
        userDto.setPassword("password123");

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setEmail(userDto.getEmail());
        savedUser.setUsername(userDto.getUsername());

        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encryptedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        ResponseEntity<ApiResponse> response = userController.registerUser(userDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Usuario creado con éxito.", response.getBody().getMessage());
        assertEquals(1, response.getBody().getId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_AssignRoleToUser() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setUsername("testuser");
        userDto.setPassword("password123");

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setEmail(userDto.getEmail());
        savedUser.setUsername(userDto.getUsername());

        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encryptedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        ResponseEntity<ApiResponse> response = userController.registerUser(userDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Usuario creado con éxito.", response.getBody().getMessage());

        // Verificar que el rol se asignó correctamente
        verify(userHasRoleRepository, times(1)).save(argThat(userHasRole ->
                userHasRole.getUserId().equals(savedUser.getId()) && userHasRole.getRoleId().equals(2)
        ));
    }

    @Test
    void testLoginUser_EmailNotFound() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("test@example.com");
        loginDto.setPassword("password123");

        when(userRepository.findByEmail(loginDto.getUsername())).thenReturn(null);

        // Act
        ResponseEntity<ApiResponse> response = userController.loginUser(loginDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("El email no existe.", response.getBody().getMessage());
        verify(userRepository, times(1)).findByEmail(loginDto.getUsername());
        verify(userRepository, never()).findByUsername(anyString());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void testLoginUser_UsernameNotFound() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testuser");
        loginDto.setPassword("password123");

        when(userRepository.findByUsername(loginDto.getUsername())).thenReturn(null);

        // Act
        ResponseEntity<ApiResponse> response = userController.loginUser(loginDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("El nombre de usuario no existe.", response.getBody().getMessage());
        verify(userRepository, times(1)).findByUsername(loginDto.getUsername());
        verify(userRepository, never()).findByEmail(anyString());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void testLoginUser_IncorrectPassword() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("test@example.com");
        loginDto.setPassword("wrongpassword");

        User user = new User();
        user.setEmail(loginDto.getUsername());
        user.setPassword("encryptedPassword");

        when(userRepository.findByEmail(loginDto.getUsername())).thenReturn(user);
        when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(false);

        // Act
        ResponseEntity<ApiResponse> response = userController.loginUser(loginDto);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("La contraseña es incorrecta.", response.getBody().getMessage());
        verify(passwordEncoder, times(1)).matches(loginDto.getPassword(), user.getPassword());
    }

    @Test
    void testLoginUser_SuccessfulLogin() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("test@example.com");
        loginDto.setPassword("correctpassword");

        User user = new User();
        user.setId(1);
        user.setEmail(loginDto.getUsername());
        user.setPassword("encryptedPassword");

        when(userRepository.findByEmail(loginDto.getUsername())).thenReturn(user);
        when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(true);

        // Act
        ResponseEntity<ApiResponse> response = userController.loginUser(loginDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login exitoso.", response.getBody().getMessage());
        assertEquals(1, response.getBody().getId());
        verify(passwordEncoder, times(1)).matches(loginDto.getPassword(), user.getPassword());
    }
}
