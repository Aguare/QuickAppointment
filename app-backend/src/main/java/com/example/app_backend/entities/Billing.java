package com.example.app_backend.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalTime;

@Entity
@Table(name = "billing")
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nit", nullable = false)
    private String nit;


    @Column(name = "cui", nullable = false)
    private String cui;

    @Column(name = "direction", nullable = false)
    private String direction;

    @Column(name = "fk_user", nullable = false)
    private int fkUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getFkUser() {
        return fkUser;
    }

    public void setFkUser(int fkUser) {
        this.fkUser = fkUser;
    }
}
