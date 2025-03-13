package com.apptware.auth.repositories;

import com.apptware.auth.models.Group;
import com.apptware.auth.models.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByOrganization(Organization organization);
    Optional<Group> findByNameAndOrganization(String name, Organization organization);
    boolean existsByNameAndOrganization(String name, Organization organization);
}
