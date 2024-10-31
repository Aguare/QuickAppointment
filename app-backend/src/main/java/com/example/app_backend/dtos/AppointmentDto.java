package com.example.app_backend.dtos;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentDto {

    private Integer id;
    private Integer fkUser;
    private Date date;
    private LocalTime hour;
    private Integer fkEmployee;
    private Integer fkType;
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
        return hour;
    }

    public void setHour(LocalTime hour) {
        this.hour = hour;
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
