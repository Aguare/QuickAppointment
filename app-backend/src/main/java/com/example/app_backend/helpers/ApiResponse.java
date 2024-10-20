package com.example.app_backend.helpers;

public class ApiResponse {
    private String message;
    private Integer id; // Agregar campo para el ID

    // Constructor para mensaje y ID
    public ApiResponse(String message, Integer id) {
        this.message = message;
        this.id = id;
    }

    // Getters y Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}


