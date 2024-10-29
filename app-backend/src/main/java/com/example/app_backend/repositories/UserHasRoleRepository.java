package com.example.app_backend.repositories;

import com.example.app_backend.dtos.PageInfoDto;
import com.example.app_backend.entities.UserHasRole;
import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserHasRoleRepository extends JpaRepository<UserHasRole, Integer> {

    UserHasRole findByFkUser(Integer fkUser);

    @Query(value = "SELECT p.id, p.name as pageName, p.direction, p.isAvailable, m.name as moduleName " +
            "FROM user_has_role uhs " +
            "LEFT JOIN role r ON r.id = uhs.FK_Role " +
            "LEFT JOIN role_has_page rhp ON rhp.FK_Role = r.id " +
            "LEFT JOIN page p ON p.id = rhp.FK_Page " +
            "LEFT JOIN module m ON m.id = p.FK_Module " +
            "WHERE uhs.FK_User = :userId ORDER BY p.id asc",
            nativeQuery = true)
    List<Object[]> findPageInfoByUserId(@Param("userId") Integer userId);


}

