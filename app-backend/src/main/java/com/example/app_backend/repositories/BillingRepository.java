package com.example.app_backend.repositories;

import com.example.app_backend.entities.Appointment;
import com.example.app_backend.entities.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface BillingRepository extends JpaRepository<Billing, Integer> {

    List<Billing> findByfkUser(Integer fkUser);
}