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

    @Query(value = "select a.id, a.date, a.hour, ty.name, ty.price, e.first_name, e.last_name, p.name, e.FK_Company, a.is_confirmated, ty.id from appointment a\n" +
            "    left join type_appointment ty on a.FK_Type = ty.id\n" +
            "    left join employee e on a.FK_Employee = e.id\n" +
            "    left join place p on a.FK_Place = p.id\n" +
            "    where a.date >= current_date and a.FK_User = :fkUser and a.is_canceled = false order by a.date asc",
            nativeQuery = true)
    List<Object[]> findReservations(Integer fkUser);

    @Query(value = "SELECT YEAR(date) AS year, COUNT(*) AS appointments\n" +
            "FROM appointment\n" +
            "GROUP BY YEAR(date)\n" +
            "ORDER BY YEAR(date)",
            nativeQuery = true)
    List<Object[]> getAppointmentByYear();

    @Query(value = "select a.id, u.username, u.email, a.date, a.hour, ty.name as service, ty.price from appointment a\n" +
            "                left join type_appointment ty on a.FK_Type = ty.id\n" +
            "                left join user u on a.FK_User = u.id\n" +
            "                order by a.date asc",
            nativeQuery = true)
    List<Object[]> allAppointments();

    @Query(value = "select a.id, u.username, u.email, a.date, a.hour, ty.name as service, ty.price from appointment a\n" +
            "                left join type_appointment ty on a.FK_Type = ty.id\n" +
            "                left join user u on a.FK_User = u.id\n" +
            "                left join company c on ty.FK_Company = c.id\n" +
            "                where c.id = :fkCompany \n" +
            "                order by a.date asc",
            nativeQuery = true)
    List<Object[]> appointmentsByCompany(Integer fkCompany);

    @Query(value = "select a.id, u.username, u.email, a.date, a.hour, ty.name as service, ty.price from appointment a\n" +
            "                left join type_appointment ty on a.FK_Type = ty.id\n" +
            "                left join user u on a.FK_User = u.id\n" +
            "                where a.is_confirmated = :status and a.is_canceled = false\n" +
            "                order by a.date asc",
            nativeQuery = true)
    List<Object[]> appointmentsByStatus(Boolean status);

    @Query(value = "select a.id, u.username, u.email, a.date, a.hour, ty.name as service, ty.price from appointment a\n" +
            "                left join type_appointment ty on a.FK_Type = ty.id\n" +
            "                left join user u on a.FK_User = u.id\n" +
            "                where  a.is_canceled = :isCanceled \n" +
            "                order by a.date asc",
            nativeQuery = true)
    List<Object[]> appointmentsCanceled(Boolean isCanceled);

    @Query(value = "select a.id, u.username, u.email, a.date, a.hour, ty.name as service, ty.price from appointment a\n" +
            "                left join type_appointment ty on a.FK_Type = ty.id\n" +
            "                left join user u on a.FK_User = u.id\n" +
            "                where a.date >= :startDate and a.date <= :endDate \n" +
            "                order by a.date asc",
            nativeQuery = true)
    List<Object[]> appointmentsByDate(Date startDate, Date endDate);

    @Query(value = "SELECT \n" +
            "    c.name AS company,\n" +
            "    COUNT(*) AS total\n" +
            "FROM \n" +
            "    appointment a\n" +
            "JOIN\n" +
            "    type_appointment t\n" +
            "on a.FK_Type = t.id\n" +
            "join\n" +
            "    company c\n" +
            "on t.FK_Company = c.id\n" +
            "GROUP BY \n" +
            "    c.name\n" +
            "ORDER BY \n" +
            "    total DESC;",
            nativeQuery = true)
    List<Object[]> getAppointmentsByCompanyReport();
}
