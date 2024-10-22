package com.example.app_backend.controllers;

import com.example.app_backend.dtos.TypeAppointmentDto;
import com.example.app_backend.entities.TypeAppointment;
import com.example.app_backend.helpers.ApiResponse;
import com.example.app_backend.repositories.TypeAppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TypeAppointmentControllerTest {

    @InjectMocks
    private TypeAppointmentController typeAppointmentController; // Clase bajo prueba

    @Mock
    private TypeAppointmentRepository typeAppointmentRepository; // Dependencia que vamos a mockear

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTypeAppointment_Success() {
        TypeAppointmentDto typeAppointmentDto = new TypeAppointmentDto();
        typeAppointmentDto.setName("Consulta General");
        typeAppointmentDto.setDescription("Descripción de la consulta general");
        typeAppointmentDto.setDuration(30);
        typeAppointmentDto.setFkCompany(1);

        TypeAppointment typeAppointment = new TypeAppointment();
        typeAppointment.setId(1);
        typeAppointment.setName(typeAppointmentDto.getName());
        typeAppointment.setDescription(typeAppointmentDto.getDescription());
        typeAppointment.setDuration(typeAppointmentDto.getDuration());
        typeAppointment.setFkCompany(typeAppointmentDto.getFkCompany());

        when(typeAppointmentRepository.save(any(TypeAppointment.class))).thenReturn(typeAppointment);

        ResponseEntity<ApiResponse> response = typeAppointmentController.createTypeAppointment(typeAppointmentDto);

        // Assert: Verificamos que la respuesta es correcta
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Servicio creado con éxito", response.getBody().getMessage());
        assertEquals(1, response.getBody().getId());

        verify(typeAppointmentRepository, times(1)).save(any(TypeAppointment.class));
    }

    @Test
    void testGetTypeAppointmentsByCompany_Success() {
        List<TypeAppointment> typeAppointments = new ArrayList<>();

        TypeAppointment typeAppointment1 = new TypeAppointment();
        typeAppointment1.setId(1);
        typeAppointment1.setName("Consulta General");
        typeAppointment1.setDescription("Descripción de consulta general");
        typeAppointment1.setDuration(30);
        typeAppointment1.setFkCompany(1);

        TypeAppointment typeAppointment2 = new TypeAppointment();
        typeAppointment2.setId(2);
        typeAppointment2.setName("Consulta Dental");
        typeAppointment2.setDescription("Descripción de consulta dental");
        typeAppointment2.setDuration(45);
        typeAppointment2.setFkCompany(1);

        typeAppointments.add(typeAppointment1);
        typeAppointments.add(typeAppointment2);

        when(typeAppointmentRepository.findByFkCompany(1)).thenReturn(typeAppointments);

        ResponseEntity<List<TypeAppointmentDto>> response = typeAppointmentController.getTypeAppointmentsByCompany(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());

        TypeAppointmentDto dto1 = response.getBody().get(0);
        assertEquals(1, dto1.getId());
        assertEquals("Consulta General", dto1.getName());
        assertEquals("Descripción de consulta general", dto1.getDescription());
        assertEquals(30, dto1.getDuration());
        assertEquals(1, dto1.getFkCompany());

        TypeAppointmentDto dto2 = response.getBody().get(1);
        assertEquals(2, dto2.getId());
        assertEquals("Consulta Dental", dto2.getName());
        assertEquals("Descripción de consulta dental", dto2.getDescription());
        assertEquals(45, dto2.getDuration());
        assertEquals(1, dto2.getFkCompany());

        verify(typeAppointmentRepository, times(1)).findByFkCompany(1);
    }

    @Test
    void testGetTypeAppointmentsByCompany_EmptyList() {
        List<TypeAppointment> emptyList = new ArrayList<>();

        when(typeAppointmentRepository.findByFkCompany(1)).thenReturn(emptyList);

        ResponseEntity<List<TypeAppointmentDto>> response = typeAppointmentController.getTypeAppointmentsByCompany(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());

        verify(typeAppointmentRepository, times(1)).findByFkCompany(1);
    }

    @Test
    void testGetTypeAppointmentById_Success() {
        TypeAppointment typeAppointment = new TypeAppointment();
        typeAppointment.setId(1);
        typeAppointment.setName("Consulta General");
        typeAppointment.setDescription("Descripción de consulta general");
        typeAppointment.setDuration(30);
        typeAppointment.setFkCompany(1);

        when(typeAppointmentRepository.findById(1)).thenReturn(Optional.of(typeAppointment));

        ResponseEntity<TypeAppointmentDto> response = typeAppointmentController.getTypeAppointmentById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
        assertEquals("Consulta General", response.getBody().getName());
        assertEquals("Descripción de consulta general", response.getBody().getDescription());
        assertEquals(30, response.getBody().getDuration());
        assertEquals(1, response.getBody().getFkCompany());

        verify(typeAppointmentRepository, times(1)).findById(1);
    }

    @Test
    void testGetTypeAppointmentById_NotFound() {
        when(typeAppointmentRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<TypeAppointmentDto> response = typeAppointmentController.getTypeAppointmentById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(typeAppointmentRepository, times(1)).findById(1);
    }

    @Test
    void testUpdateTypeAppointment_Success() {
        TypeAppointment typeAppointment = new TypeAppointment();
        typeAppointment.setId(1);
        typeAppointment.setName("Consulta General");
        typeAppointment.setDescription("Descripción de consulta general");
        typeAppointment.setDuration(30);
        typeAppointment.setFkCompany(1);

        TypeAppointmentDto updateDto = new TypeAppointmentDto();
        updateDto.setName("Consulta Actualizada");
        updateDto.setDescription("Descripción actualizada");
        updateDto.setDuration(60);
        updateDto.setFkCompany(1);

        when(typeAppointmentRepository.findById(1)).thenReturn(Optional.of(typeAppointment));

        ResponseEntity<ApiResponse> response = typeAppointmentController.updateTypeAppointment(1, updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Servicio actualizado con éxito", response.getBody().getMessage());
        assertEquals(1, response.getBody().getId());

        assertEquals("Consulta Actualizada", typeAppointment.getName());
        assertEquals("Descripción actualizada", typeAppointment.getDescription());
        assertEquals(60, typeAppointment.getDuration());

        verify(typeAppointmentRepository, times(1)).save(typeAppointment);

        verify(typeAppointmentRepository, times(1)).findById(1);
    }

    @Test
    void testUpdateTypeAppointment_NotFound() {
        when(typeAppointmentRepository.findById(1)).thenReturn(Optional.empty());

        TypeAppointmentDto updateDto = new TypeAppointmentDto();
        updateDto.setName("Consulta Actualizada");
        updateDto.setDescription("Descripción actualizada");
        updateDto.setDuration(60);
        updateDto.setFkCompany(1);

        ResponseEntity<ApiResponse> response = typeAppointmentController.updateTypeAppointment(1, updateDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Servicio no encontrado", response.getBody().getMessage());

        verify(typeAppointmentRepository, times(1)).findById(1);

        verify(typeAppointmentRepository, never()).save(any(TypeAppointment.class));
    }

    @Test
    void testDeleteTypeAppointment_Success() {
        TypeAppointment typeAppointment = new TypeAppointment();
        typeAppointment.setId(1);
        typeAppointment.setName("Consulta General");

        when(typeAppointmentRepository.findById(1)).thenReturn(Optional.of(typeAppointment));

        ResponseEntity<ApiResponse> response = typeAppointmentController.deleteTypeAppointment(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Servicio eliminado con éxito", response.getBody().getMessage());
        assertEquals(1, response.getBody().getId());

        verify(typeAppointmentRepository, times(1)).deleteById(1);

        verify(typeAppointmentRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteTypeAppointment_NotFound() {
        when(typeAppointmentRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> response = typeAppointmentController.deleteTypeAppointment(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Servicio no encontrado", response.getBody().getMessage());

        verify(typeAppointmentRepository, times(1)).findById(1);

        verify(typeAppointmentRepository, never()).deleteById(anyInt());
    }
}
