package com.apptware.auth.controllers;

import com.apptware.auth.dto.permission.PermissionRequestDTO;
import com.apptware.auth.dto.permission.PermissionResponseDTO;
import com.apptware.auth.models.Permission;
import com.apptware.auth.services.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping
    public List<PermissionResponseDTO> getAllPermissions() {
        return permissionService.findAll().stream()
                .map(PermissionResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponseDTO> getPermissionById(@PathVariable Long id) {
        return permissionService.findById(id)
                .map(PermissionResponseDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PermissionResponseDTO> getPermissionByName(@PathVariable String name) {
        return permissionService.findByName(name)
                .map(PermissionResponseDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/permission-id/{permissionId}")
    public ResponseEntity<PermissionResponseDTO> getPermissionByPermissionId(@PathVariable String permissionId) {
        return permissionService.findByPermissionId(permissionId)
                .map(PermissionResponseDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Find a permission by resources and action
     */
    @GetMapping("/by-resources-action")
    public ResponseEntity<PermissionResponseDTO> getPermissionByResourcesAndAction(
            @RequestParam("resources") String[] resources,
            @RequestParam("action") String action) {
        return permissionService.findByResourcesAndAction(resources, action)
                .map(PermissionResponseDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PermissionResponseDTO> createPermission(@Valid @RequestBody PermissionRequestDTO requestDTO) {
        // Check if a permission with these resources and action already exists
        if (permissionService.existsByResourcesAndAction(requestDTO.getResources(), requestDTO.getAction())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        Permission permission = requestDTO.toEntity();
        Permission savedPermission = permissionService.save(permission);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PermissionResponseDTO.fromEntity(savedPermission));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissionResponseDTO> updatePermission(
            @PathVariable Long id,
            @Valid @RequestBody PermissionRequestDTO requestDTO) {
        // Check if permission exists
        java.util.Optional<Permission> permissionOptional = permissionService.findById(id);
        if (permissionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Permission existingPermission = permissionOptional.get();
        
        // Check if resources/action are being changed and if they conflict with existing permission
        // Skip this check if the existing permission is being updated with its own resources/action
        String generatedName = requestDTO.getAction() + "-" + String.join(",", requestDTO.getResources());
        if (!existingPermission.getName().equals(generatedName) 
                && permissionService.existsByResourcesAndAction(requestDTO.getResources(), requestDTO.getAction())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        // Update the permission with new resources and action
        Permission updatedPermission = requestDTO.updateEntity(existingPermission);
        updatedPermission = permissionService.save(updatedPermission);
        
        return ResponseEntity.ok(PermissionResponseDTO.fromEntity(updatedPermission));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        if (permissionService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        permissionService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
