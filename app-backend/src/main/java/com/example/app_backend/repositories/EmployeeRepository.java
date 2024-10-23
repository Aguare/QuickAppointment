package com.example.app_backend.repositories;

import com.example.app_backend.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findByFkCompany(Integer fkCompany);
}

