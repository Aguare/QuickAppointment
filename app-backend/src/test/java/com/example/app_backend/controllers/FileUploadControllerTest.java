package com.example.app_backend.controllers;

import com.example.app_backend.helpers.ImgResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileUploadControllerTest {

    @InjectMocks
    private FileUploadController fileUploadController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadImage_Success() throws IOException {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "image content".getBytes());

        UUID mockUUID = UUID.randomUUID();
        String uniqueFileName = mockUUID.toString() + ".jpg";
        Path path = Paths.get(FileUploadController.UPLOAD_DIR + uniqueFileName);

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class);
             MockedStatic<UUID> mockedUUID = mockStatic(UUID.class)) {

            mockedUUID.when(UUID::randomUUID).thenReturn(mockUUID);

            mockedFiles.when(() -> Files.exists(path.getParent())).thenReturn(false);
            mockedFiles.when(() -> Files.createDirectories(path.getParent())).thenReturn(path.getParent());
            mockedFiles.when(() -> Files.write(path, file.getBytes())).thenReturn(path);

            // Act
            ResponseEntity<ImgResponse> response = fileUploadController.uploadImage(file);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Archivo subido correctamente", response.getBody().getMessage());
            assertEquals("/imgs/" + uniqueFileName, response.getBody().getRelativePath());

            mockedFiles.verify(() -> Files.exists(path.getParent()), times(1));
            mockedFiles.verify(() -> Files.createDirectories(path.getParent()), times(1));
            mockedFiles.verify(() -> Files.write(path, file.getBytes()), times(1));
        }
    }


    @Test
    void testUploadImage_FileIsEmpty() {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("file", "", "image/jpeg", new byte[0]);

        // Act
        ResponseEntity<ImgResponse> response = fileUploadController.uploadImage(file);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Por favor, selecciona un archivo", response.getBody().getMessage());
    }

    @Test
    void testUploadImage_FailureToWriteFile() throws IOException {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "image content".getBytes());

        UUID mockUUID = UUID.randomUUID();
        String uniqueFileName = mockUUID.toString() + ".jpg";
        Path path = Paths.get(FileUploadController.UPLOAD_DIR + uniqueFileName);

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class);
             MockedStatic<UUID> mockedUUID = mockStatic(UUID.class)) {

            mockedUUID.when(UUID::randomUUID).thenReturn(mockUUID);

            mockedFiles.when(() -> Files.exists(path.getParent())).thenReturn(false);
            mockedFiles.when(() -> Files.createDirectories(path.getParent())).thenReturn(path.getParent());
            mockedFiles.when(() -> Files.write(path, file.getBytes())).thenThrow(new IOException("Error de escritura"));

            // Act
            ResponseEntity<ImgResponse> response = fileUploadController.uploadImage(file);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertEquals("Error al subir el archivo", response.getBody().getMessage());
            
            mockedFiles.verify(() -> Files.exists(path.getParent()), times(1));
            mockedFiles.verify(() -> Files.createDirectories(path.getParent()), times(1));
            mockedFiles.verify(() -> Files.write(path, file.getBytes()), times(1));
        }
    }
}
