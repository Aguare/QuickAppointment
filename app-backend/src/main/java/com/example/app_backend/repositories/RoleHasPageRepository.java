package com.example.app_backend.repositories;

import com.example.app_backend.entities.Employee;
import com.example.app_backend.entities.RoleHasPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleHasPageRepository extends JpaRepository<RoleHasPage, Integer> {

    List<RoleHasPage> findByFkRole(Integer fkRole);

    @Query(value = "SELECT DISTINCT \n" +
            "    allow_create, \n" +
            "    allow_edit, \n" +
            "    allow_delete\n" +
            "FROM \n" +
            "    role_has_page\n" +
            "WHERE \n" +
            "    FK_Role = :fkRole;",
            nativeQuery = true)
    List<Object[]> getAllows(Integer fkRole);

    @Modifying
    @Query("DELETE FROM RoleHasPage rhp WHERE rhp.fkRole = :fkRole")
    void deleteByFkRole(@Param("fkRole") Integer fkRole);

}
