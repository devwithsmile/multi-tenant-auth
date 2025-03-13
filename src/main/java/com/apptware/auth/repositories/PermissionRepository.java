package com.apptware.auth.repositories;

import com.apptware.auth.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);
    Optional<Permission> findByPermissionId(String permissionId);
    boolean existsByName(String name);
    boolean existsByPermissionId(String permissionId);
}
