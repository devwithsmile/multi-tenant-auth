package com.apptware.auth.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "permission_id", unique = true, nullable = false)
    private String permissionId;

    @Column(unique = true, nullable = false)
    private String name; // Auto-generated based on action and resources like "create-user,role"
    
    @Column(name = "resources", nullable = false)
    private String resourcesStr; // Comma-separated string of resources
    
    @Transient
    private String[] resources; // Array of resources (e.g., ["user", "role"])
    
    @Column(nullable = false)
    private String action; // The action part (e.g., "create")

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "permissions")
    private Set<User> users;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "permissions")
    private Set<Group> groups;
    
    /**
     * Get resources as an array
     */
    @PostLoad
    private void onLoad() {
        if (resourcesStr != null) {
            this.resources = resourcesStr.split(",");
        }
    }
    
    /**
     * Prepare entity for persistence
     */
    @PrePersist
    @PreUpdate
    private void prePersist() {
        // Convert resources array to comma-separated string
        if (resources != null) {
            this.resourcesStr = String.join(",", resources);
        }
        
        // Auto-generate name based on action and resources
        generateName();
    }
    
    /**
     * Sets the resources array and updates resourcesStr and name
     * 
     * @param resources Array of resources
     */
    public void setResources(String[] resources) {
        this.resources = resources;
        if (resources != null) {
            this.resourcesStr = String.join(",", resources);
            generateName();
        }
    }
    
    /**
     * Sets the resources from a comma-separated string
     * 
     * @param resourcesStr Comma-separated string of resources
     */
    public void setResourcesStr(String resourcesStr) {
        this.resourcesStr = resourcesStr;
        if (resourcesStr != null && !resourcesStr.isEmpty()) {
            this.resources = resourcesStr.split(",");
            generateName();
        }
    }
    
    /**
     * Sets the action and updates the name
     * 
     * @param action The action
     */
    public void setAction(String action) {
        this.action = action;
        generateName();
    }
    
    /**
     * Generates the name based on action and resources
     * Format: "action-resource1,resource2,..."
     */
    private void generateName() {
        if (action != null && resourcesStr != null && !resourcesStr.isEmpty()) {
            this.name = action + "-" + resourcesStr;
        }
    }
    
    /**
     * Custom builder class to handle resources array and name generation
     */
    public static class PermissionBuilder {
        public PermissionBuilder resources(String[] resources) {
            this.resources = resources;
            if (resources != null) {
                this.resourcesStr = String.join(",", resources);
                generateNameForBuilder();
            }
            return this;
        }
        
        public PermissionBuilder resourcesStr(String resourcesStr) {
            this.resourcesStr = resourcesStr;
            if (resourcesStr != null && !resourcesStr.isEmpty()) {
                this.resources = resourcesStr.split(",");
                generateNameForBuilder();
            }
            return this;
        }
        
        public PermissionBuilder action(String action) {
            this.action = action;
            generateNameForBuilder();
            return this;
        }
        
        private void generateNameForBuilder() {
            if (this.action != null && this.resourcesStr != null && !this.resourcesStr.isEmpty()) {
                this.name = this.action + "-" + this.resourcesStr;
            }
        }
    }
}
