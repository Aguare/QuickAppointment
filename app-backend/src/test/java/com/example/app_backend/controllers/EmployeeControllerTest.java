package com.example.app_backend.controllers;

import com.example.app_backend.dtos.EmployeeDto;
import com.example.app_backend.entities.Employee;
import com.example.app_backend.helpers.ApiResponse;
import com.example.app_backend.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController; // Clase bajo prueba

    @Mock
    private EmployeeRepository employeeRepository; // Dependencia que vamos a mockear

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEmployee_Success() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirst_name("Juan");
        employeeDto.setLast_name("Pérez");
        employeeDto.setDpi("1234567890123");
        employeeDto.setDate_birth(LocalDate.of(1990, 1, 15));
        employeeDto.setFkCompany(1);

        Employee employee = new Employee();
        employee.setId(1);
        employee.setFirstName(employeeDto.getFirst_name());
        employee.setLastName(employeeDto.getLast_name());
        employee.setDpi(employeeDto.getDpi());
        employee.setDateBirth(employeeDto.getDate_birth());
        employee.setFkCompany(employeeDto.getFkCompany());
        employee.setIsAvailable(true);

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        ResponseEntity<ApiResponse> response = employeeController.createEmployee(employeeDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Empleado creado con éxito", response.getBody().getMessage());
        assertEquals(1, response.getBody().getId());

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testGetEmployeesByCompany_Success() {
        List<Employee> employees = new ArrayList<>();

        Employee employee1 = new Employee();
        employee1.setId(1);
        employee1.setFirstName("Juan");
        employee1.setLastName("Pérez");
        employee1.setDpi("1234567890123");
        employee1.setDateBirth(LocalDate.of(1990, 1, 15));
        employee1.setFkCompany(1);

        Employee employee2 = new Employee();
        employee2.setId(2);
        employee2.setFirstName("Ana");
        employee2.setLastName("Gómez");
        employee2.setDpi("9876543210987");
        employee2.setDateBirth(LocalDate.of(1985, 3, 20));
        employee2.setFkCompany(1);

        employees.add(employee1);
        employees.add(employee2);

        when(employeeRepository.findByFkCompany(1)).thenReturn(employees);

        ResponseEntity<List<EmployeeDto>> response = employeeController.getEmployeesByCompany(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());

        EmployeeDto dto1 = response.getBody().get(0);
        assertEquals(1, dto1.getId());
        assertEquals("Juan", dto1.getFirst_name());
        assertEquals("Pérez", dto1.getLast_name());
        assertEquals("1234567890123", dto1.getDpi());
        assertEquals(LocalDate.of(1990, 1, 15), dto1.getDate_birth());
        assertEquals(1, dto1.getFkCompany());

        EmployeeDto dto2 = response.getBody().get(1);
        assertEquals(2, dto2.getId());
        assertEquals("Ana", dto2.getFirst_name());
        assertEquals("Gómez", dto2.getLast_name());
        assertEquals("9876543210987", dto2.getDpi());
        assertEquals(LocalDate.of(1985, 3, 20), dto2.getDate_birth());
        assertEquals(1, dto2.getFkCompany());

        verify(employeeRepository, times(1)).findByFkCompany(1);
    }

    @Test
    void testGetEmployeesByCompany_EmptyList() {
        List<Employee> employees = new ArrayList<>();

        when(employeeRepository.findByFkCompany(1)).thenReturn(employees);

        ResponseEntity<List<EmployeeDto>> response = employeeController.getEmployeesByCompany(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());

        verify(employeeRepository, times(1)).findByFkCompany(1);
    }

    @Test
    void testGetEmployeeById_Success() {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setFirstName("Juan");
        employee.setLastName("Pérez");
        employee.setDpi("1234567890123");
        employee.setDateBirth(LocalDate.of(1990, 1, 15));
        employee.setFkCompany(1);

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        ResponseEntity<EmployeeDto> response = employeeController.getEmployeeById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
        assertEquals("Juan", response.getBody().getFirst_name());
        assertEquals("Pérez", response.getBody().getLast_name());
        assertEquals("1234567890123", response.getBody().getDpi());
        assertEquals(LocalDate.of(1990, 1, 15), response.getBody().getDate_birth());
        assertEquals(1, response.getBody().getFkCompany());

        verify(employeeRepository, times(1)).findById(1);
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<EmployeeDto> response = employeeController.getEmployeeById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(employeeRepository, times(1)).findById(1);
    }

    @Test
    void testUpdateEmployee_Success() {
        Employee existingEmployee = new Employee();
        existingEmployee.setId(1);
        existingEmployee.setFirstName("Juan");
        existingEmployee.setLastName("Pérez");
        existingEmployee.setDpi("1234567890123");
        existingEmployee.setDateBirth(LocalDate.of(1990, 1, 15));
        existingEmployee.setFkCompany(1);

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirst_name("Pedro");
        employeeDto.setLast_name("González");
        employeeDto.setDpi("9876543210987");
        employeeDto.setDate_birth(LocalDate.of(1985, 2, 10));
        employeeDto.setFkCompany(1);

        when(employeeRepository.findById(1)).thenReturn(Optional.of(existingEmployee));

        when(employeeRepository.save(any(Employee.class))).thenReturn(existingEmployee);

        ResponseEntity<ApiResponse> response = employeeController.updateEmployee(1, employeeDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Empleado actualizado con éxito", response.getBody().getMessage());
        assertEquals(1, response.getBody().getId());

        assertEquals("Pedro", existingEmployee.getFirstName());
        assertEquals("González", existingEmployee.getLastName());
        assertEquals("9876543210987", existingEmployee.getDpi());
        assertEquals(LocalDate.of(1985, 2, 10), existingEmployee.getDateBirth());

        verify(employeeRepository, times(1)).findById(1);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployee_NotFound() {
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirst_name("Pedro");
        employeeDto.setLast_name("González");
        employeeDto.setDpi("9876543210987");
        employeeDto.setDate_birth(LocalDate.of(1985, 2, 10));
        employeeDto.setFkCompany(1);

        ResponseEntity<ApiResponse> response = employeeController.updateEmployee(1, employeeDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Empleado no encontrado", response.getBody().getMessage());

        verify(employeeRepository, times(1)).findById(1);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void testDeleteEmployee_Success() {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setFirstName("Juan");
        employee.setLastName("Pérez");

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        ResponseEntity<ApiResponse> response = employeeController.deleteEmployee(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Empleado eliminado con éxito", response.getBody().getMessage());
        assertEquals(1, response.getBody().getId());

        verify(employeeRepository, times(1)).deleteById(1);

        verify(employeeRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteEmployee_NotFound() {
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> response = employeeController.deleteEmployee(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Empleado no encontrado", response.getBody().getMessage());

        verify(employeeRepository, times(1)).findById(1);

        verify(employeeRepository, never()).deleteById(anyInt());
    }
}
