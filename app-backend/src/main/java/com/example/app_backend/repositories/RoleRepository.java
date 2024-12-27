package com.example.app_backend.repositories;

import com.example.app_backend.entities.Place;
import com.example.app_backend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {


}