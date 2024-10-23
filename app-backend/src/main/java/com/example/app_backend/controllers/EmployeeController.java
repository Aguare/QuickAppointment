package com.example.app_backend.controllers;

import com.example.app_backend.dtos.EmployeeDto;
import com.example.app_backend.entities.Employee;
import com.example.app_backend.helpers.ApiResponse;
import com.example.app_backend.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createEmployee(@RequestBody EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setFirstName(employeeDto.getFirst_name());
        employee.setLastName(employeeDto.getLast_name());
        employee.setDpi(employeeDto.getDpi());
        employee.setDateBirth(employeeDto.getDate_birth());
        employee.setFkCompany(employeeDto.getFkCompany());
        employee.setIsAvailable(true);

        Employee savedEmployee = employeeRepository.save(employee);

        ApiResponse response = new ApiResponse("Empleado creado con éxito", savedEmployee.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/company/{fkCompany}")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByCompany(@PathVariable Integer fkCompany) {
        List<Employee> employees = employeeRepository.findByFkCompany(fkCompany);

        List<EmployeeDto> employeeDtos = employees.stream()
                .map(employee -> {
                    EmployeeDto dto = new EmployeeDto();
                    dto.setId(employee.getId());
                    dto.setFirst_name(employee.getFirstName());
                    dto.setLast_name(employee.getLastName());
                    dto.setDpi(employee.getDpi());
                    dto.setDate_birth(employee.getDateBirth());
                    dto.setFkCompany(employee.getFkCompany());
                    return dto;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(employeeDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Integer id) {

        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            EmployeeDto dto = new EmployeeDto();
            dto.setId(employee.getId());
            dto.setFirst_name(employee.getFirstName());
            dto.setLast_name(employee.getLastName());
            dto.setDpi(employee.getDpi());
            dto.setDate_birth(employee.getDateBirth());
            dto.setFkCompany(employee.getFkCompany());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateEmployee(@PathVariable Integer id, @RequestBody EmployeeDto employeeDto) {

        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            employee.setFirstName(employeeDto.getFirst_name());
            employee.setLastName(employeeDto.getLast_name());
            employee.setDpi(employeeDto.getDpi());
            employee.setDateBirth(employeeDto.getDate_birth());

            employeeRepository.save(employee);

            ApiResponse response = new ApiResponse("Empleado actualizado con éxito", employee.getId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ApiResponse response = new ApiResponse("Empleado no encontrado", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable Integer id) {

        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        if (employeeOptional.isPresent()) {
            employeeRepository.deleteById(id);
            ApiResponse response = new ApiResponse("Empleado eliminado con éxito", id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ApiResponse response = new ApiResponse("Empleado no encontrado", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
