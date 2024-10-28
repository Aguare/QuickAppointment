package com.example.app_backend.controllers;

import com.example.app_backend.dtos.*;
import com.example.app_backend.entities.Company;
import com.example.app_backend.entities.CompanySchedule;
import com.example.app_backend.helpers.ApiResponse;
import com.example.app_backend.repositories.CompanyRepository;
import com.example.app_backend.repositories.CompanyScheduleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    private static final String IMAGE_DIRECTORY = "src/main/resources/public/";
    @Autowired
    private CompanyScheduleRepository companyScheduleRepository;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCompany(@RequestBody CompanyDto companyDto) {
        Company company = new Company();
        company.setName(companyDto.getName());
        company.setDescription(companyDto.getDescription());
        company.setLogo(companyDto.getLogo());
        company.setCourtRental(companyDto.getCourtRental());
        company.setIsAvailable(true);
        company.setCreatedAt(LocalDateTime.now());
        Company savedCompany = companyRepository.save(company);
        ApiResponse response = new ApiResponse("Negocio creado con exito", savedCompany.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CompanyResponseDto>> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();

        List<CompanyResponseDto> companyDtos = companies.stream()
                .map(company -> new CompanyResponseDto(
                        company.getId(),
                        company.getName(),
                        company.getDescription(),
                        company.getLogo(),
                        company.getCourtRental()
                ))
                .collect(Collectors.toList());

        return new ResponseEntity<>(companyDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDto> getCompanyById(@PathVariable Long id) {

        Optional<Company> companyOptional = companyRepository.findById(id);

        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            CompanyResponseDto companyDto = new CompanyResponseDto(
                    company.getId(),
                    company.getName(),
                    company.getDescription(),
                    company.getLogo(),
                    company.getCourtRental()
            );
            return new ResponseEntity<>(companyDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse> updateCompany(@PathVariable Long id, @RequestBody UpdateNameDescCompanyDto updateCompanyDto) {

        Optional<Company> companyOptional = companyRepository.findById(id);

        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            company.setName(updateCompanyDto.getName());
            company.setDescription(updateCompanyDto.getDescription());
            company.setCourtRental(updateCompanyDto.getCourtRental());

            companyRepository.save(company);
            ApiResponse response = new ApiResponse("Negocio actualizado con éxito", company.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("updateLogo/{id}")
    public ResponseEntity<ApiResponse> updateCompanyLogo(@PathVariable Long id, @RequestBody UpdateCompanyLogoDto logoDto) {

        Optional<Company> companyOptional = companyRepository.findById(id);

        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();

            try {
                Path oldImagePath = Paths.get(IMAGE_DIRECTORY + logoDto.getOldPath());
                if (Files.exists(oldImagePath)) {
                    Files.delete(oldImagePath);
                    System.out.println("Eliminada");
                }
            } catch (IOException e) {
                e.printStackTrace();
                ApiResponse response = new ApiResponse("Error al eliminar imagen", null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

            company.setLogo(logoDto.getNewPath());
            companyRepository.save(company);

            ApiResponse response = new ApiResponse("Imagen Actualizada con Exito", company.getId());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCompanyById(@PathVariable Long id) {

        Optional<Company> companyOptional = companyRepository.findById(id);

        if (companyOptional.isPresent()) {
            companyRepository.deleteById(id);
            ApiResponse response = new ApiResponse("Negocio eliminado con exito", null);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            ApiResponse response = new ApiResponse("No se encontro el negocio", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/schedule/{fkCompany}")
    public ResponseEntity<List<ScheduleDto>> getCompanySchedule(@PathVariable Integer fkCompany) {
        List<CompanySchedule> schedules = companyScheduleRepository.findByFkCompany(fkCompany);

        List<ScheduleDto> scheduleDtos = schedules.stream()
                .map(schedule  -> {
                    ScheduleDto dto = new ScheduleDto();
                    dto.setFkCompany(schedule.getFkCompany());
                    dto.setFkDay(schedule.getFkDay());
                    dto.setOpeningTime(schedule.getOpeningTime());
                    dto.setClosingTime(schedule.getClosingTime());
                    return dto;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(scheduleDtos, HttpStatus.OK);
    }

    @PostMapping("/addSchedule/{fkCompany}")
    @Transactional
    public ResponseEntity<ApiResponse> updateCompanySchedule(
            @PathVariable Integer fkCompany,
            @RequestBody List<ScheduleDto> newSchedules) {

        companyScheduleRepository.deleteByFkCompany(fkCompany);

        List<CompanySchedule> schedulesToSave = newSchedules.stream()
                .map(dto -> {
                    CompanySchedule schedule = new CompanySchedule();
                    schedule.setFkCompany(dto.getFkCompany());
                    schedule.setFkDay(dto.getFkDay());
                    schedule.setOpeningTime(dto.getOpeningTime());
                    schedule.setClosingTime(dto.getClosingTime());
                    return schedule;
                })
                .collect(Collectors.toList());

        companyScheduleRepository.saveAll(schedulesToSave);

        ApiResponse response = new ApiResponse("Horarios actualizados correctamente.", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
