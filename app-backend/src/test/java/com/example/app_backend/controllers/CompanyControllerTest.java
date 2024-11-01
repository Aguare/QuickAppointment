package com.example.app_backend.controllers;

import com.example.app_backend.dtos.*;
import com.example.app_backend.entities.Company;
import com.example.app_backend.entities.CompanySchedule;
import com.example.app_backend.helpers.ApiResponse;
import com.example.app_backend.repositories.CompanyRepository;
import com.example.app_backend.repositories.CompanyScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CompanyControllerTest {

    @InjectMocks
    private CompanyController companyController; // Clase bajo prueba

    @Mock
    private CompanyScheduleRepository companyScheduleRepository;


    @Mock
    private CompanyRepository companyRepository; // Dependencia que vamos a mockear

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCompany_Success() {

        CompanyDto companyDto = new CompanyDto();
        companyDto.setName("Empresa ABC");
        companyDto.setDescription("Descripción de la Empresa ABC");
        companyDto.setLogo("logo.png");

        Company company = new Company();
        company.setId(1);
        company.setName(companyDto.getName());
        company.setDescription(companyDto.getDescription());
        company.setLogo(companyDto.getLogo());
        company.setIsAvailable(true);
        company.setCreatedAt(LocalDateTime.now());

        when(companyRepository.save(any(Company.class))).thenReturn(company);

        ResponseEntity<ApiResponse> response = companyController.createCompany(companyDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Negocio creado con exito", response.getBody().getMessage());
        assertEquals(1, response.getBody().getId());  // Asegúrate de que la verificación es con Integer

        verify(companyRepository, times(1)).save(any(Company.class));
    }

    @Test
    void testGetAllCompanies_Success() {
        List<Company> mockCompanies = new ArrayList<>();
        Company company1 = new Company();
        company1.setId(1);
        company1.setName("Empresa 1");
        company1.setDescription("Descripción 1");
        company1.setLogo("logo1.png");

        Company company2 = new Company();
        company2.setId(2);
        company2.setName("Empresa 2");
        company2.setDescription("Descripción 2");
        company2.setLogo("logo2.png");

        mockCompanies.add(company1);
        mockCompanies.add(company2);

        when(companyRepository.findAll()).thenReturn(mockCompanies);

        ResponseEntity<List<CompanyResponseDto>> response = companyController.getAllCompanies();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());

        CompanyResponseDto companyDto1 = response.getBody().get(0);
        assertEquals(1, companyDto1.getId());
        assertEquals("Empresa 1", companyDto1.getName());
        assertEquals("Descripción 1", companyDto1.getDescription());
        assertEquals("logo1.png", companyDto1.getLogo());

        CompanyResponseDto companyDto2 = response.getBody().get(1);
        assertEquals(2, companyDto2.getId());
        assertEquals("Empresa 2", companyDto2.getName());
        assertEquals("Descripción 2", companyDto2.getDescription());
        assertEquals("logo2.png", companyDto2.getLogo());

        verify(companyRepository, times(1)).findAll();
    }

    @Test
    void testGetAllCompanies_NoCompanies() {
        List<Company> mockCompanies = new ArrayList<>();

        when(companyRepository.findAll()).thenReturn(mockCompanies);

        ResponseEntity<List<CompanyResponseDto>> response = companyController.getAllCompanies();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());

        verify(companyRepository, times(1)).findAll();
    }

    @Test
    void testGetCompanyById_Success() {
        Company company = new Company();
        company.setId(1);
        company.setName("Empresa ABC");
        company.setDescription("Descripción de la Empresa ABC");
        company.setLogo("logo.png");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        ResponseEntity<CompanyResponseDto> response = companyController.getCompanyById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
        assertEquals("Empresa ABC", response.getBody().getName());
        assertEquals("Descripción de la Empresa ABC", response.getBody().getDescription());
        assertEquals("logo.png", response.getBody().getLogo());

        verify(companyRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCompanyById_NotFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<CompanyResponseDto> response = companyController.getCompanyById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(companyRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateCompany_Success() {
        Company company = new Company();
        company.setId(1);
        company.setName("Empresa ABC");
        company.setDescription("Descripción de la Empresa ABC");

        UpdateNameDescCompanyDto updateDto = new UpdateNameDescCompanyDto();
        updateDto.setName("Nuevo Nombre");
        updateDto.setDescription("Nueva Descripción");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        ResponseEntity<ApiResponse> response = companyController.updateCompany(1L, updateDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Negocio actualizado con éxito", response.getBody().getMessage());
        assertEquals(1, response.getBody().getId());

        assertEquals("Nuevo Nombre", company.getName());
        assertEquals("Nueva Descripción", company.getDescription());

        verify(companyRepository, times(1)).save(company);

        verify(companyRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateCompany_NotFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        UpdateNameDescCompanyDto updateDto = new UpdateNameDescCompanyDto();
        updateDto.setName("Nuevo Nombre");
        updateDto.setDescription("Nueva Descripción");

        ResponseEntity<ApiResponse> response = companyController.updateCompany(1L, updateDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(companyRepository, times(1)).findById(1L);

        verify(companyRepository, times(0)).save(any(Company.class));
    }

    private static final String IMAGE_DIRECTORY = "src/main/resources/public/";

    @Test
    void testUpdateCompanyLogo_Success() throws IOException {
        Company company = new Company();
        company.setId(1);
        company.setName("Empresa ABC");
        company.setLogo("old_logo.png");

        UpdateCompanyLogoDto logoDto = new UpdateCompanyLogoDto();
        logoDto.setOldPath("old_logo.png");
        logoDto.setNewPath("new_logo.png");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        Path oldImagePath = Paths.get(IMAGE_DIRECTORY + logoDto.getOldPath());

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.exists(oldImagePath)).thenReturn(true);
            mockedFiles.when(() -> Files.delete(oldImagePath)).thenAnswer(invocation -> null);

            ResponseEntity<ApiResponse> response = companyController.updateCompanyLogo(1L, logoDto);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Imagen Actualizada con Exito", response.getBody().getMessage());
            assertEquals(1, response.getBody().getId());

            assertEquals("new_logo.png", company.getLogo());

            mockedFiles.verify(() -> Files.exists(oldImagePath), times(1));
            mockedFiles.verify(() -> Files.delete(oldImagePath), times(1));
            verify(companyRepository, times(1)).save(company);
        }
    }

    @Test
    void testUpdateCompanyLogo_FileDeletionFailure() throws IOException {
        Company company = new Company();
        company.setId(1);
        company.setName("Empresa ABC");
        company.setLogo("old_logo.png");

        UpdateCompanyLogoDto logoDto = new UpdateCompanyLogoDto();
        logoDto.setOldPath("old_logo.png");
        logoDto.setNewPath("new_logo.png");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        Path oldImagePath = Paths.get(IMAGE_DIRECTORY + logoDto.getOldPath());

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.exists(oldImagePath)).thenReturn(true);
            mockedFiles.when(() -> Files.delete(oldImagePath)).thenThrow(new IOException("Error al eliminar archivo"));

            ResponseEntity<ApiResponse> response = companyController.updateCompanyLogo(1L, logoDto);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertEquals("Error al eliminar imagen", response.getBody().getMessage());

            verify(companyRepository, never()).save(company);
        }
    }

    @Test
    void testUpdateCompanyLogo_CompanyNotFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        UpdateCompanyLogoDto logoDto = new UpdateCompanyLogoDto();
        logoDto.setOldPath("old_logo.png");
        logoDto.setNewPath("new_logo.png");

        ResponseEntity<ApiResponse> response = companyController.updateCompanyLogo(1L, logoDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(companyRepository, never()).save(any(Company.class));
    }

    @Test
    void testDeleteCompanyById_Success() {
        Company company = new Company();
        company.setId(1);
        company.setName("Empresa ABC");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        ResponseEntity<ApiResponse> response = companyController.deleteCompanyById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Negocio eliminado con exito", response.getBody().getMessage());

        verify(companyRepository, times(1)).deleteById(1L);
        verify(companyRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteCompanyById_NotFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> response = companyController.deleteCompanyById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No se encontro el negocio", response.getBody().getMessage());

        verify(companyRepository, never()).deleteById(1L);
        verify(companyRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCompanySchedule() {
        // Arrange
        Integer fkCompany = 1;

        // Crear datos simulados para CompanySchedule
        CompanySchedule schedule1 = new CompanySchedule();
        schedule1.setFkCompany(1);
        schedule1.setFkDay(1);
        schedule1.setOpeningTime(LocalTime.parse("09:00"));
        schedule1.setClosingTime(LocalTime.parse("17:00"));

        CompanySchedule schedule2 = new CompanySchedule();
        schedule2.setFkCompany(1);
        schedule2.setFkDay(2);
        schedule2.setOpeningTime(LocalTime.parse("10:00"));
        schedule2.setClosingTime(LocalTime.parse("18:00"));

        when(companyScheduleRepository.findByFkCompany(fkCompany))
                .thenReturn(Arrays.asList(schedule1, schedule2));

        ResponseEntity<List<ScheduleDto>> response = companyController.getCompanySchedule(fkCompany);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        ScheduleDto scheduleDto1 = response.getBody().get(0);
        assertEquals(1, scheduleDto1.getFkCompany());
        assertEquals(1, scheduleDto1.getFkDay());
        assertEquals(LocalTime.parse("09:00"), scheduleDto1.getOpeningTime());
        assertEquals(LocalTime.parse("17:00"), scheduleDto1.getClosingTime());

        ScheduleDto scheduleDto2 = response.getBody().get(1);
        assertEquals(1, scheduleDto2.getFkCompany());
        assertEquals(2, scheduleDto2.getFkDay());
        assertEquals(LocalTime.parse("10:00"), scheduleDto2.getOpeningTime());
        assertEquals(LocalTime.parse("18:00"), scheduleDto2.getClosingTime());

        verify(companyScheduleRepository).findByFkCompany(fkCompany);
    }

    @Test
    void testGetCompanySchedule_EmptySchedule() {
        // Arrange
        Integer fkCompany = 2;

        when(companyScheduleRepository.findByFkCompany(fkCompany))
                .thenReturn(Arrays.asList());

        // Act
        ResponseEntity<List<ScheduleDto>> response = companyController.getCompanySchedule(fkCompany);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(companyScheduleRepository).findByFkCompany(fkCompany);
    }

    @Test
    void testUpdateCompanySchedule_Success() {
        // Arrange
        Integer fkCompany = 1;

        ScheduleDto scheduleDto1 = new ScheduleDto();
        scheduleDto1.setFkCompany(1);
        scheduleDto1.setFkDay(1);
        scheduleDto1.setOpeningTime(LocalTime.parse("09:00"));
        scheduleDto1.setClosingTime(LocalTime.parse("17:00"));

        ScheduleDto scheduleDto2 = new ScheduleDto();
        scheduleDto2.setFkCompany(1);
        scheduleDto2.setFkDay(2);
        scheduleDto2.setOpeningTime(LocalTime.parse("10:00"));
        scheduleDto2.setClosingTime(LocalTime.parse("18:00"));

        List<ScheduleDto> newSchedules = Arrays.asList(scheduleDto1, scheduleDto2);

        // Act
        ResponseEntity<ApiResponse> response = companyController.updateCompanySchedule(fkCompany, newSchedules);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Horarios actualizados correctamente.", response.getBody().getMessage());

        verify(companyScheduleRepository).deleteByFkCompany(fkCompany);

        verify(companyScheduleRepository).saveAll(anyList());
    }

    @Test
    void testUpdateCompanySchedule_SaveEmptyList() {
        // Arrange
        Integer fkCompany = 1;

        List<ScheduleDto> newSchedules = Arrays.asList();

        // Act
        ResponseEntity<ApiResponse> response = companyController.updateCompanySchedule(fkCompany, newSchedules);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Horarios actualizados correctamente.", response.getBody().getMessage());

        verify(companyScheduleRepository).deleteByFkCompany(fkCompany);

        verify(companyScheduleRepository).saveAll(anyList());
    }
}
