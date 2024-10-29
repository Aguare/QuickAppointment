package com.example.app_backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "company_setting")
@Getter
@Setter
public class CompanySetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "key_name", length = 40, nullable = false)
    private String key;

    @Column(name = "key_value", nullable = false)
    private String value;

    @Column(name = "key_type", length = 50, nullable = false)
    private String type;
}
