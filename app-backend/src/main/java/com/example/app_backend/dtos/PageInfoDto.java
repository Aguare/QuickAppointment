package com.example.app_backend.dtos;

public class PageInfoDto {
    private Integer id;
    private String pageName;
    private String direction;
    private Boolean isAvailable;
    private String moduleName;

    // Constructor
    public PageInfoDto(Integer id, String pageName, String direction, Boolean isAvailable, String moduleName) {
        this.id = id;
        this.pageName = pageName;
        this.direction = direction;
        this.isAvailable = isAvailable;
        this.moduleName = moduleName;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
