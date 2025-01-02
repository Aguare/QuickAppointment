package com.example.app_backend.helpers;

public class LoginResponse {
    private String message;
    private Integer idUser;
    private Integer idRole;
    private String isVerified;

    public LoginResponse(String message, Integer idUser, Integer idRole, String isVerified) {
        this.message = message;
        this.idUser = idUser;
        this.idRole = idRole;
        this.isVerified = isVerified;
    }

    public LoginResponse(String message, Integer idUser, Integer idRole) {
        this.message = message;
        this.idUser = idUser;
        this.idRole = idRole;
        this.isVerified = "0";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getIdRole() {
        return idRole;
    }

    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }

    public String getIsVerified() {
        return this.isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }
}
