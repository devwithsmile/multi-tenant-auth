package com.apptware.auth.services;

import com.apptware.auth.models.Group;
import com.apptware.auth.models.Organization;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    List<Group> findAll();
    Optional<Group> findById(Long id);
    List<Group> findByOrganization(Organization organization);
    Optional<Group> findByNameAndOrganization(String name, Organization organization);
    Group save(Group group);
    void deleteById(Long id);
    boolean existsByNameAndOrganization(String name, Organization organization);
}
