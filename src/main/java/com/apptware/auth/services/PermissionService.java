package com.apptware.auth.services;

import com.apptware.auth.models.Permission;

import java.util.List;
import java.util.Optional;

public interface PermissionService {
    List<Permission> findAll();
    Optional<Permission> findById(Long id);
    Optional<Permission> findByName(String name);
    Optional<Permission> findByPermissionId(String permissionId);
    
    /**
     * Find permissions by resources and action
     * 
     * @param resources Array of resources
     * @param action The action
     * @return Optional of Permission
     */
    Optional<Permission> findByResourcesAndAction(String[] resources, String action);
    
    Permission save(Permission permission);
    void deleteById(Long id);
    boolean existsByName(String name);
    boolean existsByPermissionId(String permissionId);
    
    /**
     * Check if a permission with the given resources and action exists
     * 
     * @param resources Array of resources
     * @param action The action
     * @return true if it exists, false otherwise
     */
    boolean existsByResourcesAndAction(String[] resources, String action);
    
    /**
     * Generates a unique permission ID in the format "perm_XXX"
     * @return A new unique permission ID
     */
    String generatePermissionId();
}
