package com.example.app_backend.dtos;

public class UpdateNameDescCompanyDto {
    private String name;
    private String description;
    private Boolean courtRental;

    // Getters y Setters
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

    public Boolean getCourtRental() {
        return courtRental;
    }

    public void setCourtRental(Boolean courtRental) {
        this.courtRental = courtRental;
    }
}
