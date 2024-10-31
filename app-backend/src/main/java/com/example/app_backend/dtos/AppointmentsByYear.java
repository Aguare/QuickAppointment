package com.example.app_backend.dtos;

public class AppointmentsByYear {

    private Integer year;
    private Integer appointments;

    public AppointmentsByYear(Integer year, Integer appointments) {
        this.year = year;
        this.appointments = appointments;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getAppointments() {
        return appointments;
    }

    public void setAppointments(Integer appointments) {
        this.appointments = appointments;
    }
}
