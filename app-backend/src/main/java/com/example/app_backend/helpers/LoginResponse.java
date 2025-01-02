package com.example.app_backend.helpers;

public class LoginResponse {
    private String message;
    private Integer idUser;
    private Integer idRole;
    private String isVerified;
    private String email;

    private Boolean is2FAEnabled;

    public LoginResponse(String message, Integer idUser, Integer idRole, String isVerified, String email, Boolean is2FAEnabled) {
        this.message = message;
        this.idUser = idUser;
        this.idRole = idRole;
        this.isVerified = isVerified;
        this.email = email;
        this.is2FAEnabled = is2FAEnabled;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIs2FAEnabled() {
        return is2FAEnabled;
    }

    public void setIs2FAEnabled(Boolean is2FAEnabled) {
        this.is2FAEnabled = is2FAEnabled;
    }
}
