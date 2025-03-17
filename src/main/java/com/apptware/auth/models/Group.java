package com.apptware.auth.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {
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
        name = "group_permissions",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "groups")
    private Set<User> users;
    
    /**
     * Utility methods for permission management
     */
    
    /**
     * Add a permission to this group
     * @param permission The permission to add
     */
    public void addPermission(Permission permission) {
        if (permissions == null) {
            permissions = new HashSet<>();
        }
        permissions.add(permission);
        
        if (permission.getGroups() == null) {
            permission.setGroups(new HashSet<>());
        }
        permission.getGroups().add(this);
    }
    
    /**
     * Remove a permission from this group
     * @param permission The permission to remove
     */
    public void removePermission(Permission permission) {
        if (permissions != null) {
            permissions.remove(permission);
        }
        
        if (permission.getGroups() != null) {
            permission.getGroups().remove(this);
        }
    }
    
    /**
     * Check if this group has a specific permission
     * @param permissionId The ID of the permission to check
     * @return true if the group has the permission
     */
    public boolean hasPermission(String permissionId) {
        if (permissions == null || permissions.isEmpty()) {
            return false;
        }
        
        return permissions.stream()
                .anyMatch(p -> p.getPermissionId().equals(permissionId));
    }
}
