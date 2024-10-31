package com.example.app_backend.dtos;

public class CompanyDto {
    private String name;
    private String description;
    private String logo;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Boolean getCourtRental() {
        return courtRental;
    }

    public void setCourtRental(Boolean courtRental) {
        this.courtRental = courtRental;
    }
}

