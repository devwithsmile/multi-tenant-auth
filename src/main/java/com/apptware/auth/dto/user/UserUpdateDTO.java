package com.apptware.auth.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

/**
 * DTO for updating User entities to prevent circular references in API requests
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    private Long id;
    
    @NotNull(message = "Organization ID is required")
    private Long organizationId;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    private String passwordHash;
    
    private Integer tokenVersion;
    
    // Instead of nested objects, use IDs for relationships
    private Set<Long> permissionIds;
    
    private Set<Long> roleIds;
    
    private Set<Long> groupIds;
    
    private Long reportingManagerId;
    
    private Boolean isAdmin;
    
    private Boolean isActive;
}
