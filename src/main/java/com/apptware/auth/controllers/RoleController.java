package com.apptware.auth.controllers;

import com.apptware.auth.dto.role.RoleRequestDTO;
import com.apptware.auth.dto.role.RoleResponseDTO;
import com.apptware.auth.dto.role.RoleSummaryDTO;
import com.apptware.auth.models.Organization;
import com.apptware.auth.models.Role;
import com.apptware.auth.services.OrganizationService;
import com.apptware.auth.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor

public class RoleController {
    private final RoleService roleService;
    private final OrganizationService organizationService;

    @GetMapping
    public List<RoleSummaryDTO> getAllRoles() {
        List<Role> roles = roleService.findAll();
        return RoleSummaryDTO.fromEntityList(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable Long id) {
        return roleService.findById(id)
                .map(role -> ResponseEntity.ok(RoleResponseDTO.fromEntity(role)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/organization/{orgId}")
    public ResponseEntity<List<RoleSummaryDTO>> getRolesByOrganization(@PathVariable Long orgId) {
        return organizationService.findById(orgId)
                .map(organization -> {
                    List<Role> roles = roleService.findByOrganization(organization);
                    return ResponseEntity.ok(RoleSummaryDTO.fromEntityList(roles));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/organization/{orgId}/name/{name}")
    public ResponseEntity<RoleResponseDTO> getRoleByNameAndOrganization(
            @PathVariable Long orgId,
            @PathVariable String name) {
        return organizationService.findById(orgId)
                .map(organization -> roleService.findByNameAndOrganization(name, organization)
                        .map(role -> ResponseEntity.ok(RoleResponseDTO.fromEntity(role)))
                        .orElse(ResponseEntity.notFound().build()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody RoleRequestDTO roleRequestDTO) {
        // Validate organization ID
        Long orgId = roleRequestDTO.getOrganizationId();
        if (orgId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Organization ID is required"));
        }
        
        // Check if organization exists
        java.util.Optional<Organization> orgOptional = organizationService.findById(orgId);
        if (orgOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Organization not found"));
        }
        
        Organization organization = orgOptional.get();
        
        // Check for duplicate role name in the same organization
        if (roleService.existsByNameAndOrganization(roleRequestDTO.getName(), organization)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Role with this name already exists in the organization"));
        }
        
        // Create the role
        Role role = roleRequestDTO.toEntity();
        role.setOrganization(organization);
        Role savedRole = roleService.save(role);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(RoleResponseDTO.fromEntity(savedRole));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(
            @PathVariable Long id,
            @RequestBody RoleRequestDTO roleRequestDTO) {
        // Check if role exists
        java.util.Optional<Role> roleOptional = roleService.findById(id);
        if (roleOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Role existingRole = roleOptional.get();
        
        // Validate organization ID
        Long orgId = roleRequestDTO.getOrganizationId();
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
        if (!existingRole.getName().equals(roleRequestDTO.getName()) 
                && roleService.existsByNameAndOrganization(roleRequestDTO.getName(), organization)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Role with this name already exists in the organization"));
        }
        
        // Update the role
        roleRequestDTO.updateEntity(existingRole);
        existingRole.setOrganization(organization);
        Role updatedRole = roleService.save(existingRole);
        
        return ResponseEntity.ok(RoleResponseDTO.fromEntity(updatedRole));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        if (!roleService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        roleService.deleteById(id);
        return ResponseEntity.ok().build();
    }
    
    @Data
    @AllArgsConstructor
    static class ErrorResponse {
        private String message;
    }
}
