package com.example.app_backend.repositories;

import com.example.app_backend.entities.Appointment;
import com.example.app_backend.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    List<Appointment> findByfkUser(Integer fkUser);

    List<Appointment> findByDate(Date date);
}
