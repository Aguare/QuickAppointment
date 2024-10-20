package com.example.app_backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "user_has_role")
public class UserHasRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "FK_User", nullable = false)
    private Integer userId;

    @Column(name = "FK_Role", nullable = false)
    private Integer roleId;

    // Constructores
    public UserHasRole() {
    }

    public UserHasRole(Integer userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    // Getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}

