package com.example.app_backend.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "FK_User", nullable = false)
    private Integer fkUser;


    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "appointment_hour", nullable = false)
    private LocalTime appointment_hour;

    @Column(name = "is_confirmated", nullable = false, columnDefinition = "TINYINT")
    private Boolean isConfirmated;

    @Column(name = "is_canceled", nullable = false, columnDefinition = "TINYINT")
    private Boolean isCanceled;

    @Column(name = "FK_Employee", nullable = false)
    private Integer fkEmployee;

    @Column(name = "FK_Type", nullable = false)
    private Integer fkType;

    @Column(name = "FK_Place", nullable = false)
    private Integer fkPlace;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFkUser() {
        return fkUser;
    }

    public void setFkUser(Integer fkUser) {
        this.fkUser = fkUser;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public LocalTime getHour() {
        return appointment_hour;
    }

    public void setHour(LocalTime hour) {
        this.appointment_hour = hour;
    }

    public Boolean getConfirmated() {
        return isConfirmated;
    }

    public void setConfirmated(Boolean confirmated) {
        isConfirmated = confirmated;
    }

    public Boolean getCanceled() {
        return isCanceled;
    }

    public void setCanceled(Boolean canceled) {
        isCanceled = canceled;
    }

    public Integer getFkEmployee() {
        return fkEmployee;
    }

    public void setFkEmployee(Integer fkEmployee) {
        this.fkEmployee = fkEmployee;
    }

    public Integer getFkType() {
        return fkType;
    }

    public void setFkType(Integer fkType) {
        this.fkType = fkType;
    }

    public Integer getFkPlace() {
        return fkPlace;
    }

    public void setFkPlace(Integer fkPlace) {
        this.fkPlace = fkPlace;
    }
}
