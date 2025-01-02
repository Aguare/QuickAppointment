package com.example.app_backend.dtos;

public class UserDto {

    private Integer id;
    private String email;
    private String username;
    private String password;
    private String rol;
    private Integer idRole;

    public UserDto(Integer id, String email, String username, String rol) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.rol = rol;
    }

    public UserDto() {}

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Integer getIdRole() {
        return idRole;
    }

    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }
}

