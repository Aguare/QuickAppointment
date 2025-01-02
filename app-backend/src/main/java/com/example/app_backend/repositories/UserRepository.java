package com.example.app_backend.repositories;

import com.example.app_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    User findByEmail(String email);
    User findByUsername(String username);

    User findUserByEmailOrUsername(String email, String username);

    @Query(value = "select u.id, u.email, u.username, r.name as rol from user u\n" +
            "left join user_has_role uhs on u.id = uhs.FK_User\n" +
            "left join role r on uhs.FK_Role = r.id",
            nativeQuery = true)
    List<Object[]> findAllUsers();

    @Query(value = "select u.id, u.email, u.username, u.password, uhs.FK_Role as idRole from user u left join user_has_role uhs on u.id = uhs.FK_User\n" +
            "    where u.id = :id", nativeQuery = true)
    Optional<Object[]> findByIdWithRoles(@Param("id") Long id);

}
