package com.example.app_backend.entities;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "company_schedule")
public class CompanySchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "FK_Company")
    private Integer fkCompany;

    @Column(name = "FK_Day")
    private Integer fkDay;

    @Column(name = "opening_time")
    private LocalTime openingTime;

    @Column(name = "closing_time")
    private LocalTime closingTime;

    public CompanySchedule() {
    }

    public CompanySchedule(Integer fkCompany, Integer fkDay, LocalTime openingTime, LocalTime closingTime) {
        this.fkCompany = fkCompany;
        this.fkDay = fkDay;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFkCompany() {
        return fkCompany;
    }

    public void setFkCompany(Integer fkCompany) {
        this.fkCompany = fkCompany;
    }

    public Integer getFkDay() {
        return fkDay;
    }

    public void setFkDay(Integer fkDay) {
        this.fkDay = fkDay;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }
}
