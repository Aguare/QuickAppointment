package com.example.app_backend.helpers;

public class ImgResponse {

    private String message;
    private String absolutePath;
    private String relativePath;

    // Constructor
    public ImgResponse(String message, String absolutePath, String relativePath) {
        this.message = message;
        this.absolutePath = absolutePath;
        this.relativePath = relativePath;
    }

    // Getters y Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

}
