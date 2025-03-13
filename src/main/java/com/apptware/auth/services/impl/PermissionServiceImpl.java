package com.apptware.auth.services.impl;

import com.apptware.auth.models.Permission;
import com.apptware.auth.repositories.PermissionRepository;
import com.apptware.auth.services.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final Random random = new Random();

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    public Optional<Permission> findByName(String name) {
        return permissionRepository.findByName(name);
    }
    
    @Override
    public Optional<Permission> findByPermissionId(String permissionId) {
        return permissionRepository.findByPermissionId(permissionId);
    }
    
    @Override
    public Optional<Permission> findByResourcesAndAction(String[] resources, String action) {
        if (resources == null || resources.length == 0 || action == null || action.isEmpty()) {
            return Optional.empty();
        }
        
        // Sort resources to ensure consistent format
        String[] sortedResources = Arrays.copyOf(resources, resources.length);
        Arrays.sort(sortedResources);
        String resourcesStr = String.join(",", sortedResources);
        
        // Generate the name that would be created based on these resources and action
        String generatedName = action + "-" + resourcesStr;
        
        // Find by the generated name
        return permissionRepository.findByName(generatedName);
    }

    @Override
    public Permission save(Permission permission) {
        // Generate a unique permission ID if not present
        if (permission.getPermissionId() == null || permission.getPermissionId().isEmpty()) {
            permission.setPermissionId(generatePermissionId());
        }
        
        // If resources array is provided but resourcesStr is empty, convert and sort it
        if (permission.getResources() != null && permission.getResources().length > 0 && 
            (permission.getResourcesStr() == null || permission.getResourcesStr().isEmpty())) {
            String[] sortedResources = Arrays.copyOf(permission.getResources(), permission.getResources().length);
            Arrays.sort(sortedResources);
            permission.setResourcesStr(String.join(",", sortedResources));
        }
        
        return permissionRepository.save(permission);
    }

    @Override
    public void deleteById(Long id) {
        permissionRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return permissionRepository.existsByName(name);
    }
    
    @Override
    public boolean existsByPermissionId(String permissionId) {
        return permissionRepository.existsByPermissionId(permissionId);
    }
    
    @Override
    public boolean existsByResourcesAndAction(String[] resources, String action) {
        if (resources == null || resources.length == 0 || action == null || action.isEmpty()) {
            return false;
        }
        
        // Sort resources to ensure consistent format
        String[] sortedResources = Arrays.copyOf(resources, resources.length);
        Arrays.sort(sortedResources);
        String resourcesStr = String.join(",", sortedResources);
        
        // Generate the name that would be created based on these resources and action
        String generatedName = action + "-" + resourcesStr;
        
        // Check if a permission with this name exists
        return permissionRepository.existsByName(generatedName);
    }
    
    @Override
    public String generatePermissionId() {
        String permissionId;
        do {
            // Generate a random number between 100 and 999
            int randomNum = 100 + random.nextInt(900);
            permissionId = "perm_" + randomNum;
        } while (existsByPermissionId(permissionId));
        
        return permissionId;
    }
}
