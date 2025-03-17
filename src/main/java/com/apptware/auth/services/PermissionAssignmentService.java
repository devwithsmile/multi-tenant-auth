package com.apptware.auth.services;

import com.apptware.auth.models.Group;
import com.apptware.auth.models.Permission;
import com.apptware.auth.models.Role;
import com.apptware.auth.models.User;

import java.util.List;
import java.util.Set;

/**
 * Service for managing permission assignments to users, roles, and groups
 */
public interface PermissionAssignmentService {
    /**
     * Assign permissions to a user
     * 
     * @param user The user to assign permissions to
     * @param permissions The permissions to assign
     * @return The updated user with assigned permissions
     */
    User assignPermissionsToUser(User user, Set<Permission> permissions);
    
    /**
     * Remove permissions from a user
     * 
     * @param user The user to remove permissions from
     * @param permissions The permissions to remove
     * @return The updated user with permissions removed
     */
    User removePermissionsFromUser(User user, Set<Permission> permissions);
    
    /**
     * Get all permissions assigned to a user (direct permissions only, not from roles or groups)
     * 
     * @param user The user
     * @return The list of permissions assigned to the user
     */
    List<Permission> getUserPermissions(User user);
    
    /**
     * Get all effective permissions for a user (including those from roles and groups)
     * 
     * @param user The user
     * @return The list of effective permissions for the user
     */
    List<Permission> getUserEffectivePermissions(User user);
    
    /**
     * Assign permissions to a role
     * 
     * @param role The role to assign permissions to
     * @param permissions The permissions to assign
     * @return The updated role with assigned permissions
     */
    Role assignPermissionsToRole(Role role, Set<Permission> permissions);
    
    /**
     * Remove permissions from a role
     * 
     * @param role The role to remove permissions from
     * @param permissions The permissions to remove
     * @return The updated role with permissions removed
     */
    Role removePermissionsFromRole(Role role, Set<Permission> permissions);
    
    /**
     * Get all permissions assigned to a role
     * 
     * @param role The role
     * @return The list of permissions assigned to the role
     */
    List<Permission> getRolePermissions(Role role);
    
    /**
     * Assign permissions to a group
     * 
     * @param group The group to assign permissions to
     * @param permissions The permissions to assign
     * @return The updated group with assigned permissions
     */
    Group assignPermissionsToGroup(Group group, Set<Permission> permissions);
    
    /**
     * Remove permissions from a group
     * 
     * @param group The group to remove permissions from
     * @param permissions The permissions to remove
     * @return The updated group with permissions removed
     */
    Group removePermissionsFromGroup(Group group, Set<Permission> permissions);
    
    /**
     * Get all permissions assigned to a group
     * 
     * @param group The group
     * @return The list of permissions assigned to the group
     */
    List<Permission> getGroupPermissions(Group group);
    
    /**
     * Check if a user has a specific permission (directly, through roles, or through groups)
     * 
     * @param user The user
     * @param permissionId The permission ID to check
     * @return true if the user has the permission, false otherwise
     */
    boolean userHasPermission(User user, String permissionId);
}
