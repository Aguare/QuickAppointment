package com.example.app_backend.dtos;

public class CompanyResponseDto {
    private Integer id;
    private String name;
    private String description;
    private String logo;
    private Boolean courtRental;

    // Constructor
    public CompanyResponseDto(Integer id, String name, String description, String logo, Boolean courtRental) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.logo = logo;
        this.courtRental = courtRental;
    }

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Boolean getCourtRental() {
        return courtRental;
    }
}

