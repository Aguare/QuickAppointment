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

    public MyAppointmentsDto() {
    }

    public MyAppointmentsDto(Integer id, Date date, Time hour, String service, Double price, String first_name, String last_name, String place) {
        this.id = id;
        this.date = date;
        this.hour = hour;
        this.service = service;
        this.price = price;
        this.first_name = first_name;
        this.last_name = last_name;
        this.place = place;
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
}
