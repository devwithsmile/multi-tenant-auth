package com.apptware.auth.models;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "org_id")
    @JsonBackReference
    private Organization organization;
    
    // Method to get organization ID for JSON serialization
    public Long getOrganizationId() {
        return organization != null ? organization.getId() : null;
    }

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(name = "is_admin")
    private boolean isAdmin = false;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "token_version")
    private Integer tokenVersion = 0;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
        name = "user_permissions",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
        name = "user_groups",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<Group> groups;

    @Column(name = "reporting_manager_id")
    private Long reportingManagerId;

    @Column(name = "is_active")
    private boolean isActive = true;
    
    /**
     * Utility methods for permission management
     */
    
    /**
     * Add a permission directly to this user
     * @param permission The permission to add
     */
    public void addPermission(Permission permission) {
        if (permissions == null) {
            permissions = new HashSet<>();
        }
        permissions.add(permission);
        
        if (permission.getUsers() == null) {
            permission.setUsers(new HashSet<>());
        }
        permission.getUsers().add(this);
    }
    
    /**
     * Remove a permission from this user
     * @param permission The permission to remove
     */
    public void removePermission(Permission permission) {
        if (permissions != null) {
            permissions.remove(permission);
        }
        
        if (permission.getUsers() != null) {
            permission.getUsers().remove(this);
        }
    }
    
    /**
     * Check if this user has a specific permission directly assigned
     * @param permissionId The ID of the permission to check
     * @return true if the user has the permission directly assigned
     */
    public boolean hasDirectPermission(String permissionId) {
        if (permissions == null || permissions.isEmpty()) {
            return false;
        }
        
        return permissions.stream()
                .anyMatch(p -> p.getPermissionId().equals(permissionId));
    }
    
    /**
     * Check if this user has a permission through any means (direct, role, or group)
     * @param permissionId The ID of the permission to check
     * @return true if the user has the permission
     */
    public boolean hasPermission(String permissionId) {
        // Check direct permissions
        if (hasDirectPermission(permissionId)) {
            return true;
        }
        
        // Check role permissions
        if (roles != null && !roles.isEmpty()) {
            for (Role role : roles) {
                if (role.hasPermission(permissionId)) {
                    return true;
                }
            }
        }
        
        // Check group permissions
        if (groups != null && !groups.isEmpty()) {
            for (Group group : groups) {
                if (group.hasPermission(permissionId)) {
                    return true;
                }
            }
        }
        
        return false;
    }
}
