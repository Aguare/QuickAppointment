package com.example.app_backend.dtos;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;

public class MyAppointmentsDto {
    private Integer id;
    private Date date;
    private Time hour;
    private String service;
    private Double price;
    private String first_name;
    private String last_name;
    private String place;
    private Integer fkCompany;
    private Boolean is_confirmated;
    private Integer fkService;

    public MyAppointmentsDto() {
    }

    public MyAppointmentsDto(Integer id, Date date, Time hour, String service, Double price, String first_name, String last_name, String place, Integer fkCompany, Boolean is_confirmated, Integer fkService) {
        this.id = id;
        this.date = date;
        this.hour = hour;
        this.service = service;
        this.price = price;
        this.first_name = first_name;
        this.last_name = last_name;
        this.place = place;
        this.fkCompany = fkCompany;
        this.is_confirmated = is_confirmated;
        this.fkService = fkService;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getHour() {
        return hour;
    }

    public void setHour(Time hour) {
        this.hour = hour;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getFkCompany() {
        return fkCompany;
    }

    public void setFkCompany(Integer fkCompany) {
        this.fkCompany = fkCompany;
    }

    public Boolean getIs_confirmated() {
        return is_confirmated;
    }

    public void setIs_confirmated(Boolean is_confirmated) {
        this.is_confirmated = is_confirmated;
    }

    public Integer getFkService() {
        return fkService;
    }

    public void setFkService(Integer fkService) {
        this.fkService = fkService;
    }
}
