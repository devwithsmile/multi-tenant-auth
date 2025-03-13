package com.apptware.auth.dto.user;

import com.apptware.auth.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {
    @NotNull(message = "Organization ID is required")
    private Long organizationId;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    private String password;
    
    private boolean isAdmin;
    
    public User toEntity() {
        return User.builder()
                .email(this.email)
                .isAdmin(this.isAdmin)
                .passwordHash(this.password) // Note: In a real application, this would be hashed before saving
                .build();
    }
    
    public User updateEntity(User user) {
        user.setEmail(this.email);
        user.setAdmin(this.isAdmin);
        
        // Only update password if provided
        if (this.password != null && !this.password.trim().isEmpty()) {
            user.setPasswordHash(this.password); // Note: In a real application, this would be hashed before saving
        }
        
        return user;
    }
}
