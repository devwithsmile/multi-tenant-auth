package com.apptware.auth.services.impl;

import com.apptware.auth.models.Group;
import com.apptware.auth.models.Organization;
import com.apptware.auth.repositories.GroupRepository;
import com.apptware.auth.services.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public Optional<Group> findById(Long id) {
        return groupRepository.findById(id);
    }

    @Override
    public List<Group> findByOrganization(Organization organization) {
        return groupRepository.findByOrganization(organization);
    }

    @Override
    public Optional<Group> findByNameAndOrganization(String name, Organization organization) {
        return groupRepository.findByNameAndOrganization(name, organization);
    }

    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public void deleteById(Long id) {
        groupRepository.deleteById(id);
    }

    @Override
    public boolean existsByNameAndOrganization(String name, Organization organization) {
        return groupRepository.existsByNameAndOrganization(name, organization);
    }
}
