package com.apptware.auth.services.impl;

import com.apptware.auth.models.Organization;
import com.apptware.auth.repositories.OrganizationRepository;
import com.apptware.auth.services.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;

    @Override
    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    @Override
    public Optional<Organization> findById(Long id) {
        return organizationRepository.findById(id);
    }

    @Override
    public Optional<Organization> findByName(String name) {
        return organizationRepository.findByName(name);
    }

    @Override
    public Organization save(Organization organization) {
        return organizationRepository.save(organization);
    }

    @Override
    public void deleteById(Long id) {
        organizationRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return organizationRepository.existsByName(name);
    }
}
