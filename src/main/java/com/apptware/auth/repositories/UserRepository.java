package com.apptware.auth.repositories;

import com.apptware.auth.models.Organization;
import com.apptware.auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByOrganization(Organization organization);
    boolean existsByEmail(String email);
    List<User> findByIsAdminTrue();

    @Query("SELECT u FROM User u " +
    "WHERE u.reportingManagerId = (SELECT u2.reportingManagerId FROM User u2 WHERE u2.id = :userId) " +
    "AND u.id <> :userId")
    List<User> findColleagues(@Param("userId") Long userId);

}

