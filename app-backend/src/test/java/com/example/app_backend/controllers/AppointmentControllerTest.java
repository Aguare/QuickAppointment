package com.example.app_backend.controllers;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

import com.example.app_backend.dtos.AppointmentDto;
import com.example.app_backend.entities.Appointment;
import com.example.app_backend.helpers.ApiResponse;
import com.example.app_backend.repositories.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AppointmentControllerTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentController appointmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAppointment_Success() {
        // Arrange
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setFkUser(1);
        LocalDate localDate = LocalDate.parse("2024-10-30");
        Date sqlDate = Date.valueOf(localDate);
        appointmentDto.setDate(sqlDate);
        appointmentDto.setHour(LocalTime.parse("10:30"));
        appointmentDto.setFkEmployee(2);
        appointmentDto.setFkType(3);
        appointmentDto.setFkPlace(4);

        Appointment savedAppointment = new Appointment();
        savedAppointment.setId(1);
        savedAppointment.setFkUser(1);
        LocalDate localDate1 = LocalDate.parse("2024-10-30");
        Date sqlDate1 = Date.valueOf(localDate);
        savedAppointment.setDate(sqlDate1);
        savedAppointment.setHour(LocalTime.parse("10:30"));
        savedAppointment.setConfirmated(true);
        savedAppointment.setCanceled(false);
        savedAppointment.setFkEmployee(2);
        savedAppointment.setFkType(3);
        savedAppointment.setFkPlace(4);

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedAppointment);

        // Act
        ResponseEntity<ApiResponse> response = appointmentController.createAppointment(appointmentDto);

        // Assert
        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Cita creada con exito", response.getBody().getMessage());
        assertEquals(1, response.getBody().getId());

        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void testGetAppointmentsByUser_Success() {
        // Arrange
        Integer fkUser = 1;

        Appointment appointment1 = new Appointment();
        appointment1.setId(1);
        appointment1.setFkUser(fkUser);
        appointment1.setDate(Date.valueOf(LocalDate.parse("2024-10-30")));
        appointment1.setHour(LocalTime.parse("10:30"));
        appointment1.setFkEmployee(2);
        appointment1.setFkType(3);

        Appointment appointment2 = new Appointment();
        appointment2.setId(2);
        appointment2.setFkUser(fkUser);
        appointment2.setDate(Date.valueOf(LocalDate.parse("2024-11-05")));
        appointment2.setHour(LocalTime.parse("11:00"));
        appointment2.setFkEmployee(3);
        appointment2.setFkType(4);

        List<Appointment> appointments = Arrays.asList(appointment1, appointment2);

        when(appointmentRepository.findByfkUser(fkUser)).thenReturn(appointments);

        // Act
        ResponseEntity<List<AppointmentDto>> response = appointmentController.getAppointmentsByUser(fkUser);

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        AppointmentDto dto1 = response.getBody().get(0);
        assertEquals(1, dto1.getId());
        assertEquals(Date.valueOf(LocalDate.parse("2024-10-30")), dto1.getDate());
        assertEquals(LocalTime.parse("10:30"), dto1.getHour());
        assertEquals(2, dto1.getFkEmployee());
        assertEquals(3, dto1.getFkType());

        AppointmentDto dto2 = response.getBody().get(1);
        assertEquals(2, dto2.getId());
        assertEquals(Date.valueOf(LocalDate.parse("2024-11-05")), dto2.getDate());
        assertEquals(LocalTime.parse("11:00"), dto2.getHour());
        assertEquals(3, dto2.getFkEmployee());
        assertEquals(4, dto2.getFkType());

        verify(appointmentRepository).findByfkUser(fkUser);
    }

    @Test
    void testGetAppointmentsByUser_NoAppointments() {
        // Arrange
        Integer fkUser = 2;

        when(appointmentRepository.findByfkUser(fkUser)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<AppointmentDto>> response = appointmentController.getAppointmentsByUser(fkUser);

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(appointmentRepository).findByfkUser(fkUser);
    }

    @Test
    void testGetAppointmentsByDate_Success() {
        // Arrange
        Date date = Date.valueOf(LocalDate.parse("2024-10-30"));

        Appointment appointment1 = new Appointment();
        appointment1.setId(1);
        appointment1.setDate(date);
        appointment1.setHour(LocalTime.parse("10:30"));
        appointment1.setFkEmployee(2);
        appointment1.setFkType(3);
        appointment1.setFkPlace(1);

        Appointment appointment2 = new Appointment();
        appointment2.setId(2);
        appointment2.setDate(date);
        appointment2.setHour(LocalTime.parse("11:00"));
        appointment2.setFkEmployee(3);
        appointment2.setFkType(4);
        appointment2.setFkPlace(2);

        List<Appointment> appointments = Arrays.asList(appointment1, appointment2);

        when(appointmentRepository.findByDate(date)).thenReturn(appointments);

        // Act
        ResponseEntity<List<AppointmentDto>> response = appointmentController.getAppointmentsByDate(date);

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        AppointmentDto dto1 = response.getBody().get(0);
        assertEquals(1, dto1.getId());
        assertEquals(date, dto1.getDate());
        assertEquals(LocalTime.parse("10:30"), dto1.getHour());
        assertEquals(2, dto1.getFkEmployee());
        assertEquals(3, dto1.getFkType());
        assertEquals(1, dto1.getFkPlace());

        AppointmentDto dto2 = response.getBody().get(1);
        assertEquals(2, dto2.getId());
        assertEquals(date, dto2.getDate());
        assertEquals(LocalTime.parse("11:00"), dto2.getHour());
        assertEquals(3, dto2.getFkEmployee());
        assertEquals(4, dto2.getFkType());
        assertEquals(2, dto2.getFkPlace());

        verify(appointmentRepository).findByDate(date);
    }

    @Test
    void testGetAppointmentsByDate_NoAppointments() {
        // Arrange
        Date date = Date.valueOf(LocalDate.parse("2024-10-30"));

        when(appointmentRepository.findByDate(date)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<AppointmentDto>> response = appointmentController.getAppointmentsByDate(date);

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(appointmentRepository).findByDate(date);
    }
}
