package com.example.app_backend.dtos;

public class UpdateCompanyLogoDto {
    private String oldPath;
    private String newPath;

    // Getters y Setters
    public String getOldPath() {
        return oldPath;
    }

    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
    }

    public String getNewPath() {
        return newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }
}
