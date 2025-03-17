package com.apptware.auth.controllers;

import com.apptware.auth.dto.group.GroupRequestDTO;
import com.apptware.auth.dto.group.GroupResponseDTO;
import com.apptware.auth.dto.group.GroupSummaryDTO;
import com.apptware.auth.models.Group;
import com.apptware.auth.models.Organization;
import com.apptware.auth.services.GroupService;
import com.apptware.auth.services.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor

public class GroupController {
    private final GroupService groupService;
    private final OrganizationService organizationService;

    @GetMapping
    public List<GroupSummaryDTO> getAllGroups() {
        List<Group> groups = groupService.findAll();
        return GroupSummaryDTO.fromEntityList(groups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponseDTO> getGroupById(@PathVariable Long id) {
        return groupService.findById(id)
                .map(group -> ResponseEntity.ok(GroupResponseDTO.fromEntity(group)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/organization/{orgId}")
    public ResponseEntity<List<GroupSummaryDTO>> getGroupsByOrganization(@PathVariable Long orgId) {
        return organizationService.findById(orgId)
                .map(organization -> {
                    List<Group> groups = groupService.findByOrganization(organization);
                    return ResponseEntity.ok(GroupSummaryDTO.fromEntityList(groups));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/organization/{orgId}/name/{name}")
    public ResponseEntity<GroupResponseDTO> getGroupByNameAndOrganization(
            @PathVariable Long orgId,
            @PathVariable String name) {
        return organizationService.findById(orgId)
                .map(organization -> groupService.findByNameAndOrganization(name, organization)
                        .map(group -> ResponseEntity.ok(GroupResponseDTO.fromEntity(group)))
                        .orElse(ResponseEntity.notFound().build()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody GroupRequestDTO groupRequestDTO) {
        // Validate organization ID
        Long orgId = groupRequestDTO.getOrganizationId();
        if (orgId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Organization ID is required"));
        }
        
        // Check if organization exists
        java.util.Optional<Organization> orgOptional = organizationService.findById(orgId);
        if (orgOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Organization not found"));
        }
        
        Organization organization = orgOptional.get();
        
        // Check for duplicate group name in the same organization
        if (groupService.existsByNameAndOrganization(groupRequestDTO.getName(), organization)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Group with this name already exists in the organization"));
        }
        
        // Create the group
        Group group = groupRequestDTO.toEntity();
        group.setOrganization(organization);
        Group savedGroup = groupService.save(group);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(GroupResponseDTO.fromEntity(savedGroup));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGroup(
            @PathVariable Long id,
            @RequestBody GroupRequestDTO groupRequestDTO) {
        // Check if group exists
        java.util.Optional<Group> groupOptional = groupService.findById(id);
        if (groupOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Group existingGroup = groupOptional.get();
        
        // Validate organization ID
        Long orgId = groupRequestDTO.getOrganizationId();
        if (orgId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Organization ID is required"));
        }
        
        // Check if organization exists
        java.util.Optional<Organization> orgOptional = organizationService.findById(orgId);
        if (orgOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Organization not found"));
        }
        
        Organization organization = orgOptional.get();
        
        // Check if name is being changed and if it conflicts
        if (!existingGroup.getName().equals(groupRequestDTO.getName()) 
                && groupService.existsByNameAndOrganization(groupRequestDTO.getName(), organization)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Group with this name already exists in the organization"));
        }
        
        // Update the group
        groupRequestDTO.updateEntity(existingGroup);
        existingGroup.setOrganization(organization);
        Group updatedGroup = groupService.save(existingGroup);
        
        return ResponseEntity.ok(GroupResponseDTO.fromEntity(updatedGroup));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        if (!groupService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        groupService.deleteById(id);
        return ResponseEntity.ok().build();
    }
    
    @Data
    @AllArgsConstructor
    static class ErrorResponse {
        private String message;
    }
}
