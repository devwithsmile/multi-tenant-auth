package com.apptware.auth.dto.permission;

import com.apptware.auth.models.Permission;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionRequestDTO {
    
    /**
     * An array of resources this permission applies to
     * Example: ["user", "role"]
     */
    @NotEmpty(message = "At least one resource is required")
    private String[] resources;
    
    /**
     * The action to be performed on the resources
     * Example: "create", "read", "update", "delete"
     */
    @NotBlank(message = "Action is required")
    private String action;
    
    /**
     * Convert the DTO to a Permission entity
     */
    public Permission toEntity() {
        return Permission.builder()
                .resources(this.resources)
                .action(this.action)
                .build();
    }
    
    /**
     * Update an existing Permission entity with values from this DTO
     */
    public Permission updateEntity(Permission permission) {
        permission.setResources(this.resources);
        permission.setAction(this.action);
        return permission;
    }
}
