package com.example.app_backend.dtos;

public class TypeAppointmentDto {
    private Integer id;
    private String name;
    private String description;
    private Integer duration;
    private Integer fkCompany;

    // Getters y Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getFkCompany() {
        return fkCompany;
    }

    public void setFkCompany(Integer fkCompany) {
        this.fkCompany = fkCompany;
    }
}

