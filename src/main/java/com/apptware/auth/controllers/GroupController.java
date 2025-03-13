package com.apptware.auth.controllers;

import com.apptware.auth.models.Group;
import com.apptware.auth.models.Organization;
import com.apptware.auth.services.GroupService;
import com.apptware.auth.services.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class GroupController {
    private final GroupService groupService;
    private final OrganizationService organizationService;

    @GetMapping
    public List<Group> getAllGroups() {
        return groupService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        return groupService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.<Group>notFound().build());
    }

    @GetMapping("/organization/{orgId}")
    public ResponseEntity<List<Group>> getGroupsByOrganization(@PathVariable Long orgId) {
        return organizationService.findById(orgId)
                .map(organization -> {
                    List<Group> groups = groupService.findByOrganization(organization);
                    return ResponseEntity.ok(groups);
                })
                .orElse(ResponseEntity.<List<Group>>notFound().build());
    }

    @GetMapping("/organization/{orgId}/name/{name}")
    public ResponseEntity<Group> getGroupByNameAndOrganization(
            @PathVariable Long orgId,
            @PathVariable String name) {
        return organizationService.findById(orgId)
                .map(organization -> groupService.findByNameAndOrganization(name, organization)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.<Group>notFound().build()))
                .orElse(ResponseEntity.<Group>notFound().build());
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        // Validate organization if provided
        if (group.getOrganization() == null || group.getOrganization().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
        Long orgId = group.getOrganization().getId();
        java.util.Optional<Organization> orgOptional = organizationService.findById(orgId);
        if (!orgOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Organization organization = orgOptional.get();
        if (groupService.existsByNameAndOrganization(group.getName(), organization)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        group.setOrganization(organization); // Ensure we have the full organization object
        Group savedGroup = groupService.save(group);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGroup);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(
            @PathVariable Long id,
            @RequestBody Group group) {
        // Check if group exists
        java.util.Optional<Group> groupOptional = groupService.findById(id);
        if (!groupOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Group existingGroup = groupOptional.get();
        
        // Validate organization
        if (group.getOrganization() == null || group.getOrganization().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
        Long orgId = group.getOrganization().getId();
        java.util.Optional<Organization> orgOptional = organizationService.findById(orgId);
        if (!orgOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Organization organization = orgOptional.get();
        
        // Check if name is being changed and if it conflicts
        if (!existingGroup.getName().equals(group.getName()) 
                && groupService.existsByNameAndOrganization(group.getName(), organization)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        group.setId(id);
        group.setOrganization(organization); // Ensure we have the full organization object
        Group updatedGroup = groupService.save(group);
        return ResponseEntity.ok(updatedGroup);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        if (!groupService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        groupService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
