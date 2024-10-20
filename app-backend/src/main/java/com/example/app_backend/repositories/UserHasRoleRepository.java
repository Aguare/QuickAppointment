package com.example.app_backend.repositories;

import com.example.app_backend.entities.UserHasRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHasRoleRepository extends JpaRepository<UserHasRole, Integer> {
}

