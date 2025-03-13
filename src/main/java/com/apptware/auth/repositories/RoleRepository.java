package com.apptware.auth.repositories;

import com.apptware.auth.models.Organization;
import com.apptware.auth.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByOrganization(Organization organization);
    Optional<Role> findByNameAndOrganization(String name, Organization organization);
    boolean existsByNameAndOrganization(String name, Organization organization);
}
