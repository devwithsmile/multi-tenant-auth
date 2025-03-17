package com.apptware.auth.dto.permission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Response DTO for permission assignments
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionAssignmentResponseDTO {
    private Long entityId;  // User, Role, or Group ID
    private String entityType;  // "USER", "ROLE", or "GROUP"
    private Set<PermissionResponseDTO> assignedPermissions;
    private Set<PermissionResponseDTO> removedPermissions;
    private String message;
    private boolean success;
}
