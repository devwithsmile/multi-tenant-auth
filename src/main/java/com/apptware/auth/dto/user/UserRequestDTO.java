package com.apptware.auth.dto.user;

import com.apptware.auth.models.Organization;
import com.apptware.auth.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating or updating a user
 * Contains only the essential fields required for user creation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {
    /**
     * The organization ID this user belongs to
     */
    @NotNull(message = "Organization ID is required")
    private Long organizationId;
    
    @NotBlank(message = "Name is required")
    private String name;

    /**
     * User's email address (used as username)
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    /**
     * User's password (will be hashed before storage)
     */
    private String password;
    
    /**
     * Whether this user has admin privileges
     */
    @Builder.Default
    private boolean isAdmin = false;
    
    /**
     * ID of the user's reporting manager (optional)
     */
    private Long reportingManagerId;
    
    /**
     * Whether the user account is active
     */
    @Builder.Default
    private boolean isActive = true;
    
    /**
     * Convert the DTO to a User entity
     * Note: Organization will need to be set separately
     */
    public User toEntity() {
        Organization org = new Organization();
        org.setId(this.organizationId);
        
        return User.builder()
                .organization(org)
                .name(this.name)
                .email(this.email)
                .isAdmin(this.isAdmin)
                .passwordHash(this.password) // Note: In a real application, this would be hashed before saving
                .reportingManagerId(this.reportingManagerId)
                .isActive(this.isActive)
                .tokenVersion(0) // Initialize tokenVersion to prevent null pointer exception
                .build();
    }
    
    /**
     * Update an existing User entity with values from this DTO
     */
    public User updateEntity(User user) {
        user.setName(this.name);
        user.setEmail(this.email);
        user.setAdmin(this.isAdmin);
        user.setActive(this.isActive);
        
        // Update reporting manager if provided
        if (this.reportingManagerId != null) {
            user.setReportingManagerId(this.reportingManagerId);
        }
        
        // Only update password if provided
        if (this.password != null && !this.password.trim().isEmpty()) {
            user.setPasswordHash(this.password); // Note: In a real application, this would be hashed before saving
        }
        
        return user;
    }
}
