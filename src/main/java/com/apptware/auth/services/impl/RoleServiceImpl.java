package com.apptware.auth.services.impl;

import com.apptware.auth.models.Organization;
import com.apptware.auth.models.Role;
import com.apptware.auth.repositories.RoleRepository;
import com.apptware.auth.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public List<Role> findByOrganization(Organization organization) {
        return roleRepository.findByOrganization(organization);
    }

    @Override
    public Optional<Role> findByNameAndOrganization(String name, Organization organization) {
        return roleRepository.findByNameAndOrganization(name, organization);
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public boolean existsByNameAndOrganization(String name, Organization organization) {
        return roleRepository.existsByNameAndOrganization(name, organization);
    }
}
