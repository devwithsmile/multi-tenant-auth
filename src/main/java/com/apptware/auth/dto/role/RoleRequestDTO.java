package com.apptware.auth.dto.role;

import com.apptware.auth.models.Role;
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
public class RoleRequestDTO {
    @NotNull(message = "Organization ID is required")
    private Long organizationId;
    
    @NotBlank(message = "Role name is required")
    private String name;
    
    public Role toEntity() {
        return Role.builder()
                .name(this.name)
                .build();
    }
    
    public Role updateEntity(Role role) {
        role.setName(this.name);
        return role;
    }
}
