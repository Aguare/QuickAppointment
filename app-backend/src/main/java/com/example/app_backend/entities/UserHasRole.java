package com.example.app_backend.entities;

import com.example.app_backend.dtos.PageInfoDto;
import jakarta.persistence.*;

@Entity
@Table(name = "user_has_role")
public class UserHasRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "FK_User")
    private Integer fkUser;

    @Column(name = "FK_Role")
    private Integer fkRole;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFkUser() {
        return fkUser;
    }

    public void setFkUser(Integer fkUser) {
        this.fkUser = fkUser;
    }

    public Integer getFkRole() {
        return fkRole;
    }

    public void setFkRole(Integer fkRole) {
        this.fkRole = fkRole;
    }
}

