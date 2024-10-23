package com.example.app_backend.repositories;

import com.example.app_backend.entities.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Integer> {

    List<Place> findByFkCompany(Integer fkCompany);
}
