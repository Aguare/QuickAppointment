package com.example.app_backend.repositories;

import com.example.app_backend.entities.Company;
import com.example.app_backend.entities.CompanySetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Objects;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {


}
