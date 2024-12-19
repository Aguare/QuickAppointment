package com.example.app_backend.dtos;

import java.sql.Date;
import java.sql.Time;

public class AppointmentReportDto {

    private Integer id;
    private String username;
    private String email;
    private Date date;
    private Time hour;
    private String service;
    private Double price;

    public AppointmentReportDto(Integer id, String username, String email, Date date, Time hour, String service, Double price) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.date = date;
        this.hour = hour;
        this.service = service;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
