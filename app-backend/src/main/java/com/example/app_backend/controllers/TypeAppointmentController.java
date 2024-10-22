package com.example.app_backend.controllers;

import com.example.app_backend.dtos.TypeAppointmentDto;
import com.example.app_backend.entities.TypeAppointment;
import com.example.app_backend.helpers.ApiResponse;
import com.example.app_backend.repositories.TypeAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/type-appointments")
public class TypeAppointmentController {

    @Autowired
    private TypeAppointmentRepository typeAppointmentRepository;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createTypeAppointment(@RequestBody TypeAppointmentDto typeAppointmentDto) {

        TypeAppointment typeAppointment = new TypeAppointment();
        typeAppointment.setName(typeAppointmentDto.getName());
        typeAppointment.setDescription(typeAppointmentDto.getDescription());
        typeAppointment.setDuration(typeAppointmentDto.getDuration());
        typeAppointment.setFkCompany(typeAppointmentDto.getFkCompany());

        TypeAppointment savedTypeAppointment = typeAppointmentRepository.save(typeAppointment);

        ApiResponse response = new ApiResponse("Servicio creado con éxito", savedTypeAppointment.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/company/{fkCompany}")
    public ResponseEntity<List<TypeAppointmentDto>> getTypeAppointmentsByCompany(@PathVariable Integer fkCompany) {

        List<TypeAppointment> typeAppointments = typeAppointmentRepository.findByFkCompany(fkCompany);

        List<TypeAppointmentDto> typeAppointmentDtos = typeAppointments.stream()
                .map(typeAppointment -> {
                    TypeAppointmentDto dto = new TypeAppointmentDto();
                    dto.setId(typeAppointment.getId());
                    dto.setName(typeAppointment.getName());
                    dto.setDescription(typeAppointment.getDescription());
                    dto.setDuration(typeAppointment.getDuration());
                    dto.setFkCompany(typeAppointment.getFkCompany());
                    return dto;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(typeAppointmentDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeAppointmentDto> getTypeAppointmentById(@PathVariable Integer id) {
        Optional<TypeAppointment> typeAppointmentOptional = typeAppointmentRepository.findById(id);

        if (typeAppointmentOptional.isPresent()) {
            TypeAppointment typeAppointment = typeAppointmentOptional.get();
            TypeAppointmentDto dto = new TypeAppointmentDto();
            dto.setId(typeAppointment.getId());
            dto.setName(typeAppointment.getName());
            dto.setDescription(typeAppointment.getDescription());
            dto.setDuration(typeAppointment.getDuration());
            dto.setFkCompany(typeAppointment.getFkCompany());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateTypeAppointment(
            @PathVariable Integer id,
            @RequestBody TypeAppointmentDto updateTypeAppointmentDto) {

        Optional<TypeAppointment> typeAppointmentOptional = typeAppointmentRepository.findById(id);

        if (typeAppointmentOptional.isPresent()) {
            TypeAppointment typeAppointment = typeAppointmentOptional.get();
            typeAppointment.setName(updateTypeAppointmentDto.getName());
            typeAppointment.setDescription(updateTypeAppointmentDto.getDescription());
            typeAppointment.setDuration(updateTypeAppointmentDto.getDuration());

            typeAppointmentRepository.save(typeAppointment);

            ApiResponse response = new ApiResponse("Servicio actualizado con éxito", typeAppointment.getId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ApiResponse response = new ApiResponse("Servicio no encontrado", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTypeAppointment(@PathVariable Integer id) {

        Optional<TypeAppointment> typeAppointmentOptional = typeAppointmentRepository.findById(id);

        if (typeAppointmentOptional.isPresent()) {
            typeAppointmentRepository.deleteById(id);
            ApiResponse response = new ApiResponse("Servicio eliminado con éxito", id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ApiResponse response = new ApiResponse("Servicio no encontrado", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
