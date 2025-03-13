package com.apptware.auth.controllers;

import com.apptware.auth.models.Organization;
import com.apptware.auth.models.Role;
import com.apptware.auth.services.OrganizationService;
import com.apptware.auth.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class RoleController {
    private final RoleService roleService;
    private final OrganizationService organizationService;

    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        return roleService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.<Role>notFound().build());
    }

    @GetMapping("/organization/{orgId}")
    public ResponseEntity<List<Role>> getRolesByOrganization(@PathVariable Long orgId) {
        return organizationService.findById(orgId)
                .map(organization -> {
                    List<Role> roles = roleService.findByOrganization(organization);
                    return ResponseEntity.ok(roles);
                })
                .orElse(ResponseEntity.<List<Role>>notFound().build());
    }

    @GetMapping("/organization/{orgId}/name/{name}")
    public ResponseEntity<Role> getRoleByNameAndOrganization(
            @PathVariable Long orgId,
            @PathVariable String name) {
        return organizationService.findById(orgId)
                .map(organization -> roleService.findByNameAndOrganization(name, organization)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.<Role>notFound().build()))
                .orElse(ResponseEntity.<Role>notFound().build());
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        // Validate organization if provided
        if (role.getOrganization() == null || role.getOrganization().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
        Long orgId = role.getOrganization().getId();
        java.util.Optional<Organization> orgOptional = organizationService.findById(orgId);
        if (!orgOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Organization organization = orgOptional.get();
        if (roleService.existsByNameAndOrganization(role.getName(), organization)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        role.setOrganization(organization); // Ensure we have the full organization object
        Role savedRole = roleService.save(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(
            @PathVariable Long id,
            @RequestBody Role role) {
        // Check if role exists
        java.util.Optional<Role> roleOptional = roleService.findById(id);
        if (!roleOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Role existingRole = roleOptional.get();
        
        // Validate organization
        if (role.getOrganization() == null || role.getOrganization().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
        Long orgId = role.getOrganization().getId();
        java.util.Optional<Organization> orgOptional = organizationService.findById(orgId);
        if (!orgOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Organization organization = orgOptional.get();
        
        // Check if name is being changed and if it conflicts
        if (!existingRole.getName().equals(role.getName()) 
                && roleService.existsByNameAndOrganization(role.getName(), organization)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        role.setId(id);
        role.setOrganization(organization); // Ensure we have the full organization object
        Role updatedRole = roleService.save(role);
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        if (!roleService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        roleService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
