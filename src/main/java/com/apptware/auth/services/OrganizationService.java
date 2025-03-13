package com.apptware.auth.services;

import com.apptware.auth.models.Organization;

import java.util.List;
import java.util.Optional;

public interface OrganizationService {
    List<Organization> findAll();
    Optional<Organization> findById(Long id);
    Optional<Organization> findByName(String name);
    Organization save(Organization organization);
    void deleteById(Long id);
    boolean existsByName(String name);
}
