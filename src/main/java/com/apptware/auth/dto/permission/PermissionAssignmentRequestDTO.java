package com.apptware.auth.dto.permission;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * DTO for requesting permission assignments to users, roles, or groups
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionAssignmentRequestDTO {
    /**
     * IDs of the permissions to assign
     */
    @NotEmpty(message = "At least one permission ID must be provided")
    private Set<String> permissionIds;
}
