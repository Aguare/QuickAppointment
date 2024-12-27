package com.example.app_backend.dtos;

public class RoleDto {

    private Integer id;
    private String name;
    private String description;
    private Boolean allowCreate;
    private Boolean allowDelete;
    private Boolean allowEdit;

    public RoleDto(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public RoleDto(){}

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

    public Boolean getAllowCreate() {
        return allowCreate;
    }

    public void setAllowCreate(Boolean allowCreate) {
        this.allowCreate = allowCreate;
    }

    public Boolean getAllowDelete() {
        return allowDelete;
    }

    public void setAllowDelete(Boolean allowDelete) {
        this.allowDelete = allowDelete;
    }

    public Boolean getAllowEdit() {
        return allowEdit;
    }

    public void setAllowEdit(Boolean allowEdit) {
        this.allowEdit = allowEdit;
    }
}
