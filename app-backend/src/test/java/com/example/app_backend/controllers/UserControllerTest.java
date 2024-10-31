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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserHasRoleRepository userHasRoleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    @Mock
    private SendEmailController sendEmailController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("test@test.com");
        userDto.setUsername("testUser");
        userDto.setPassword("password123");

        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

        // Act
        ResponseEntity<ApiResponse> response = userController.registerUser(userDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("El email ya está en uso.", response.getBody().getMessage());
        verify(userRepository).existsByEmail(userDto.getEmail());
    }

    @Test
    void testRegisterUser_UsernameAlreadyExists() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("test@test.com");
        userDto.setUsername("testUser");
        userDto.setPassword("password123");

        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(true);

        // Act
        ResponseEntity<ApiResponse> response = userController.registerUser(userDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("El nombre de usuario ya está en uso.", response.getBody().getMessage());
        verify(userRepository).existsByUsername(userDto.getUsername());
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setUsername("username");
        userDto.setPassword("password");

        User user = new User();
        user.setId(1);
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());

        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encryptedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        ResponseEntity<ApiResponse> response = userController.registerUser(userDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Usuario creado con éxito.", response.getBody().getMessage());
        assertEquals(1, response.getBody().getId());

        // Verificar llamadas de métodos
        verify(userRepository).existsByEmail(userDto.getEmail());
        verify(userRepository).existsByUsername(userDto.getUsername());
        verify(passwordEncoder).encode(userDto.getPassword());
        verify(userRepository).save(any(User.class));
        verify(userHasRoleRepository).save(any(UserHasRole.class));
        verify(sendEmailController).sendEmailVerification(user.getEmail());
    }

    @Test
    void testLoginUser_EmailNotFound() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("test@test.com");
        loginDto.setPassword("password123");

        when(userRepository.findByEmail(anyString())).thenReturn(null);

        // Act
        ResponseEntity<LoginResponse> response = userController.loginUser(loginDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("El email no existe.", response.getBody().getMessage());
        verify(userRepository).findByEmail(loginDto.getUsername());
    }

    @Test
    void testLoginUser_UsernameNotFound() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testUser");
        loginDto.setPassword("password123");

        when(userRepository.findByUsername(anyString())).thenReturn(null);

        // Act
        ResponseEntity<LoginResponse> response = userController.loginUser(loginDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("El nombre de usuario no existe.", response.getBody().getMessage());
        verify(userRepository).findByUsername(loginDto.getUsername());
    }

    @Test
    void testLoginUser_IncorrectPassword() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testUser");
        loginDto.setPassword("wrongPassword");

        User user = new User();
        user.setId(1);
        user.setPassword("correctEncodedPassword");

        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(false);

        // Act
        ResponseEntity<LoginResponse> response = userController.loginUser(loginDto);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("La contraseña es incorrecta.", response.getBody().getMessage());
        verify(passwordEncoder).matches(loginDto.getPassword(), user.getPassword());
    }

    @Test
    void testLoginUser_NoRoleFound() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testUser");
        loginDto.setPassword("password123");

        User user = new User();
        user.setId(1);
        user.setPassword("correctEncodedPassword");

        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(true);
        when(userHasRoleRepository.findByFkUser(user.getId())).thenReturn(null);

        // Act
        ResponseEntity<LoginResponse> response = userController.loginUser(loginDto);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("No se encontró un rol asociado al usuario.", response.getBody().getMessage());
        verify(userHasRoleRepository).findByFkUser(user.getId());
    }

    @Test
    void testLoginUser_SuccessfulLogin() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testUser");
        loginDto.setPassword("password123");

        User user = new User();
        user.setId(1);
        user.setPassword("correctEncodedPassword");

        UserHasRole userHasRole = new UserHasRole();
        userHasRole.setFkUser(1);
        userHasRole.setFkRole(2);

        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(true);
        when(userHasRoleRepository.findByFkUser(user.getId())).thenReturn(userHasRole);

        // Act
        ResponseEntity<LoginResponse> response = userController.loginUser(loginDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login exitoso", response.getBody().getMessage());
        assertEquals(1, response.getBody().getIdUser());
        assertEquals(2, response.getBody().getIdRole());
        verify(userRepository).findByUsername(loginDto.getUsername());
        verify(passwordEncoder).matches(loginDto.getPassword(), user.getPassword());
        verify(userHasRoleRepository).findByFkUser(user.getId());
    }

    @Test
    void testGetPagesByUserId_UserHasPages() {
        // Arrange
        Integer userId = 7;
        List<Object[]> mockResult = Arrays.asList(
                new Object[]{1, "Dashboard", "/dashboard", true, "Main Module"},
                new Object[]{2, "Settings", "/settings", true, "Settings Module"}
        );

        when(userHasRoleRepository.findPageInfoByUserId(userId)).thenReturn(mockResult);

        // Act
        ResponseEntity<List<PageInfoDto>> response = userController.getPagesByUserId(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        PageInfoDto page1 = response.getBody().get(0);
        assertEquals(1, page1.getId());
        assertEquals("Dashboard", page1.getPageName());
        assertEquals("/dashboard", page1.getDirection());
        assertTrue(page1.getIsAvailable());
        assertEquals("Main Module", page1.getModuleName());

        PageInfoDto page2 = response.getBody().get(1);
        assertEquals(2, page2.getId());
        assertEquals("Settings", page2.getPageName());
        assertEquals("/settings", page2.getDirection());
        assertTrue(page2.getIsAvailable());
        assertEquals("Settings Module", page2.getModuleName());

        verify(userHasRoleRepository).findPageInfoByUserId(userId);
    }

    @Test
    void testGetPagesByUserId_UserHasNoPages() {
        // Arrange
        Integer userId = 7;
        when(userHasRoleRepository.findPageInfoByUserId(userId)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<PageInfoDto>> response = userController.getPagesByUserId(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(userHasRoleRepository).findPageInfoByUserId(userId);
    }
}
