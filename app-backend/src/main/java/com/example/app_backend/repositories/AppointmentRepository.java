package com.example.app_backend.repositories;

import com.example.app_backend.entities.Appointment;
import com.example.app_backend.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    List<Appointment> findByfkUser(Integer fkUser);

    List<Appointment> findByDate(Date date);

    @Query(value = "select a.id, a.date, a.hour, ty.name, ty.price, e.first_name, e.last_name, p.name from appointment a\n" +
            "    left join type_appointment ty on a.FK_Type = ty.id\n" +
            "    left join employee e on a.FK_Employee = e.id\n" +
            "    left join place p on a.FK_Place = p.id\n" +
            "    where a.date > current_date",
            nativeQuery = true)
    List<Object[]> findReservations();
}
