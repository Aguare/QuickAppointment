package com.example.app_backend.repositories;

import com.example.app_backend.entities.CompanySetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanySettingRepository extends JpaRepository<CompanySetting, Integer> {

    Optional<CompanySetting> findByKey(String keyName);

    List<CompanySetting> findAllByKeyIn(List<String> keyNames);
}
