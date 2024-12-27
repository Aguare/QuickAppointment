package com.example.app_backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "role_has_page")
public class RoleHasPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "FK_Role")
    private Integer fkRole;

    @Column(name = "FK_Page")
    private Integer fkPage;

    @Column(name = "allow_create")
    private Boolean allowCreate;

    @Column(name = "allow_edit")
    private Boolean allowEdit;

    @Column(name = "allow_delete")
    private Boolean allowDelete;

    public RoleHasPage(Integer id, Integer fkRole, Integer fkPage, Boolean allowCreate, Boolean allowEdit, Boolean allowDelete) {
        this.id = id;
        this.fkRole = fkRole;
        this.fkPage = fkPage;
        this.allowCreate = allowCreate;
        this.allowEdit = allowEdit;
        this.allowDelete = allowDelete;
    }

    public RoleHasPage() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFkRole() {
        return fkRole;
    }

    public void setFkRole(Integer fkRole) {
        this.fkRole = fkRole;
    }

    public Integer getFkPage() {
        return fkPage;
    }

    public void setFkPage(Integer fkPage) {
        this.fkPage = fkPage;
    }

    public Boolean getAllowCreate() {
        return allowCreate;
    }

    public void setAllowCreate(Boolean allowCreate) {
        this.allowCreate = allowCreate;
    }

    public Boolean getAllowEdit() {
        return allowEdit;
    }

    public void setAllowEdit(Boolean allowEdit) {
        this.allowEdit = allowEdit;
    }

    public Boolean getAllowDelete() {
        return allowDelete;
    }

    public void setAllowDelete(Boolean allowDelete) {
        this.allowDelete = allowDelete;
    }
}
