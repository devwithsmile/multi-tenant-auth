package com.apptware.auth.services;

import com.apptware.auth.models.Organization;
import com.apptware.auth.models.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAll();
    Optional<Role> findById(Long id);
    List<Role> findByOrganization(Organization organization);
    Optional<Role> findByNameAndOrganization(String name, Organization organization);
    Role save(Role role);
    void deleteById(Long id);
    boolean existsByNameAndOrganization(String name, Organization organization);
}
