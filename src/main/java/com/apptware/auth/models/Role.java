package com.apptware.auth.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

    @Column(nullable = false)
    private String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
        name = "role_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
    
    /**
     * Utility methods for permission management
     */
    
    /**
     * Add a permission to this role
     * @param permission The permission to add
     */
    public void addPermission(Permission permission) {
        if (permissions == null) {
            permissions = new HashSet<>();
        }
        permissions.add(permission);
        
        if (permission.getRoles() == null) {
            permission.setRoles(new HashSet<>());
        }
        permission.getRoles().add(this);
    }
    
    /**
     * Remove a permission from this role
     * @param permission The permission to remove
     */
    public void removePermission(Permission permission) {
        if (permissions != null) {
            permissions.remove(permission);
        }
        
        if (permission.getRoles() != null) {
            permission.getRoles().remove(this);
        }
    }
    
    /**
     * Check if this role has a specific permission
     * @param permissionId The ID of the permission to check
     * @return true if the role has the permission
     */
    public boolean hasPermission(String permissionId) {
        if (permissions == null || permissions.isEmpty()) {
            return false;
        }
        
        return permissions.stream()
                .anyMatch(p -> p.getPermissionId().equals(permissionId));
    }
}
