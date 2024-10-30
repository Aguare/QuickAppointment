package com.example.app_backend.controllers;

import com.example.app_backend.dtos.AppointmentDto;
import com.example.app_backend.dtos.EmployeeDto;
import com.example.app_backend.entities.Appointment;
import com.example.app_backend.helpers.ApiResponse;
import com.example.app_backend.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createAppointment(@RequestBody AppointmentDto appointmentDto) {
        Appointment appointment = new Appointment();
        appointment.setFkUser(appointmentDto.getFkUser());
        appointment.setDate(appointmentDto.getDate());
        appointment.setHour(appointmentDto.getHour());
        appointment.setConfirmated(true);
        appointment.setCanceled(false);
        appointment.setFkEmployee(appointmentDto.getFkEmployee());
        appointment.setFkType(appointmentDto.getFkType());
        appointment.setFkPlace(appointmentDto.getFkPlace());

        Appointment savedAppointment = appointmentRepository.save(appointment);
        ApiResponse response = new ApiResponse("Cita creada con exito", savedAppointment.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/{fkUser}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByUser(@PathVariable Integer fkUser) {
        List<Appointment> appointments = appointmentRepository.findByfkUser(fkUser);

        List<AppointmentDto> appointmentDtos = appointments.stream()
                .map(appointment -> {
                    AppointmentDto dto = new AppointmentDto();
                    dto.setId(appointment.getId());
                    dto.setDate(appointment.getDate());
                    dto.setHour(appointment.getHour());
                    dto.setFkEmployee(appointment.getFkEmployee());
                    dto.setFkType(appointment.getFkType());
                    return dto;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(appointmentDtos, HttpStatus.OK);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByDate(@PathVariable Date date) {
        List<Appointment> appointments = appointmentRepository.findByDate(date);

        List<AppointmentDto> appointmentDtos = appointments.stream()
                .map(appointment -> {
                    AppointmentDto dto = new AppointmentDto();
                    dto.setId(appointment.getId());
                    dto.setDate(appointment.getDate());
                    dto.setHour(appointment.getHour());
                    dto.setFkEmployee(appointment.getFkEmployee());
                    dto.setFkType(appointment.getFkType());
                    dto.setFkPlace(appointment.getFkPlace());
                    return dto;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(appointmentDtos, HttpStatus.OK);
    }
}
