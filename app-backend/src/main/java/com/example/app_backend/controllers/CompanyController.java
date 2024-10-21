package com.example.app_backend.controllers;

import com.example.app_backend.dtos.CompanyDto;
import com.example.app_backend.dtos.CompanyResponseDto;
import com.example.app_backend.dtos.UpdateCompanyLogoDto;
import com.example.app_backend.dtos.UpdateNameDescCompanyDto;
import com.example.app_backend.entities.Company;
import com.example.app_backend.helpers.ApiResponse;
import com.example.app_backend.repositories.CompanyRepository;
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

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCompany(@RequestBody CompanyDto companyDto) {
        Company company = new Company();
        company.setName(companyDto.getName());
        company.setDescription(companyDto.getDescription());
        company.setLogo(companyDto.getLogo());
        company.setIsAvailable(true);
        company.setCreatedAt(LocalDateTime.now());

        companyRepository.save(company);
        ApiResponse response = new ApiResponse("Negocio creado con exito", company.getId());
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
                        company.getLogo()
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
                    company.getLogo()
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
}
