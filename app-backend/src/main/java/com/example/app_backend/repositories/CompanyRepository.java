package com.example.app_backend.repositories;

import com.example.app_backend.entities.Company;
import com.example.app_backend.entities.CompanySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {


}
