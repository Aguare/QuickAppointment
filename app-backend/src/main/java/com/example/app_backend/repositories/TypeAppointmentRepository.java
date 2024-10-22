package com.example.app_backend.repositories;

import com.example.app_backend.entities.TypeAppointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TypeAppointmentRepository extends JpaRepository<TypeAppointment, Integer> {

    List<TypeAppointment> findByFkCompany(Integer fkCompany);

}

