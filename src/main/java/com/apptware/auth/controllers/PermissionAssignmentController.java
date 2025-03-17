package com.apptware.auth.controllers;

import com.apptware.auth.dto.permission.PermissionAssignmentRequestDTO;
import com.apptware.auth.dto.permission.PermissionAssignmentResponseDTO;
import com.apptware.auth.dto.permission.PermissionResponseDTO;
import com.apptware.auth.models.Group;
import com.apptware.auth.models.Permission;
import com.apptware.auth.models.Role;
import com.apptware.auth.models.User;
import com.apptware.auth.services.GroupService;
import com.apptware.auth.services.PermissionAssignmentService;
import com.apptware.auth.services.PermissionService;
import com.apptware.auth.services.RoleService;
import com.apptware.auth.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/permissions-assignment")
@RequiredArgsConstructor
public class PermissionAssignmentController {
    private final PermissionAssignmentService permissionAssignmentService;
    private final PermissionService permissionService;
    private final UserService userService;
    private final RoleService roleService;
    private final GroupService groupService;

    /**
     * Get all permissions assigned to a user
     */
    @GetMapping("/users/{userId}/permissions")
    public ResponseEntity<List<PermissionResponseDTO>> getUserPermissions(@PathVariable Long userId) {
        return userService.findById(userId)
                .map(user -> {
                    List<Permission> permissions = permissionAssignmentService.getUserPermissions(user);
                    List<PermissionResponseDTO> dtos = permissions.stream()
                            .map(PermissionResponseDTO::fromEntity)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(dtos);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all effective permissions for a user (including those from roles and groups)
     */
    @GetMapping("/users/{userId}/effective-permissions")
    public ResponseEntity<List<PermissionResponseDTO>> getUserEffectivePermissions(@PathVariable Long userId) {
        return userService.findById(userId)
                .map(user -> {
                    List<Permission> permissions = permissionAssignmentService.getUserEffectivePermissions(user);
                    List<PermissionResponseDTO> dtos = permissions.stream()
                            .map(PermissionResponseDTO::fromEntity)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(dtos);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Assign permissions to a user
     */
    @PostMapping("/users/{userId}/permissions")
    public ResponseEntity<PermissionAssignmentResponseDTO> assignPermissionsToUser(
            @PathVariable Long userId,
            @Valid @RequestBody PermissionAssignmentRequestDTO requestDTO) {
        return userService.findById(userId)
                .map(user -> {
                    Set<Permission> permissionsToAssign = new HashSet<>();
                    Set<PermissionResponseDTO> assignedPermissions = new HashSet<>();
                    
                    // Find all permissions by their IDs
                    for (String permissionId : requestDTO.getPermissionIds()) {
                        permissionService.findByPermissionId(permissionId).ifPresent(permission -> {
                            permissionsToAssign.add(permission);
                            assignedPermissions.add(PermissionResponseDTO.fromEntity(permission));
                        });
                    }
                    
                    if (permissionsToAssign.isEmpty()) {
                        return ResponseEntity.badRequest().body(
                                PermissionAssignmentResponseDTO.builder()
                                        .entityId(userId)
                                        .entityType("USER")
                                        .success(false)
                                        .message("No valid permissions found to assign")
                                        .build()
                        );
                    }
                    
                    // Assign permissions
                    User updatedUser = permissionAssignmentService.assignPermissionsToUser(user, permissionsToAssign);
                    
                    return ResponseEntity.ok(
                            PermissionAssignmentResponseDTO.builder()
                                    .entityId(updatedUser.getId())
                                    .entityType("USER")
                                    .assignedPermissions(assignedPermissions)
                                    .success(true)
                                    .message("Permissions assigned successfully")
                                    .build()
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Remove permissions from a user
     */
    @DeleteMapping("/users/{userId}/permissions")
    public ResponseEntity<PermissionAssignmentResponseDTO> removePermissionsFromUser(
            @PathVariable Long userId,
            @Valid @RequestBody PermissionAssignmentRequestDTO requestDTO) {
        return userService.findById(userId)
                .map(user -> {
                    Set<Permission> permissionsToRemove = new HashSet<>();
                    Set<PermissionResponseDTO> removedPermissions = new HashSet<>();
                    
                    // Find all permissions by their IDs
                    for (String permissionId : requestDTO.getPermissionIds()) {
                        permissionService.findByPermissionId(permissionId).ifPresent(permission -> {
                            permissionsToRemove.add(permission);
                            removedPermissions.add(PermissionResponseDTO.fromEntity(permission));
                        });
                    }
                    
                    if (permissionsToRemove.isEmpty()) {
                        return ResponseEntity.badRequest().body(
                                PermissionAssignmentResponseDTO.builder()
                                        .entityId(userId)
                                        .entityType("USER")
                                        .success(false)
                                        .message("No valid permissions found to remove")
                                        .build()
                        );
                    }
                    
                    // Remove permissions
                    User updatedUser = permissionAssignmentService.removePermissionsFromUser(user, permissionsToRemove);
                    
                    return ResponseEntity.ok(
                            PermissionAssignmentResponseDTO.builder()
                                    .entityId(updatedUser.getId())
                                    .entityType("USER")
                                    .removedPermissions(removedPermissions)
                                    .success(true)
                                    .message("Permissions removed successfully")
                                    .build()
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Check if a user has a specific permission
     */
    @GetMapping("/users/{userId}/has-permission/{permissionId}")
    public ResponseEntity<Boolean> checkUserPermission(
            @PathVariable Long userId,
            @PathVariable String permissionId) {
        return userService.findById(userId)
                .map(user -> {
                    boolean hasPermission = permissionAssignmentService.userHasPermission(user, permissionId);
                    return ResponseEntity.ok(hasPermission);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all permissions assigned to a role
     */
    @GetMapping("/roles/{roleId}/permissions")
    public ResponseEntity<List<PermissionResponseDTO>> getRolePermissions(@PathVariable Long roleId) {
        return roleService.findById(roleId)
                .map(role -> {
                    List<Permission> permissions = permissionAssignmentService.getRolePermissions(role);
                    List<PermissionResponseDTO> dtos = permissions.stream()
                            .map(PermissionResponseDTO::fromEntity)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(dtos);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Assign permissions to a role
     */
    @PostMapping("/roles/{roleId}/permissions")
    public ResponseEntity<PermissionAssignmentResponseDTO> assignPermissionsToRole(
            @PathVariable Long roleId,
            @Valid @RequestBody PermissionAssignmentRequestDTO requestDTO) {
        return roleService.findById(roleId)
                .map(role -> {
                    Set<Permission> permissionsToAssign = new HashSet<>();
                    Set<PermissionResponseDTO> assignedPermissions = new HashSet<>();
                    
                    // Find all permissions by their IDs
                    for (String permissionId : requestDTO.getPermissionIds()) {
                        permissionService.findByPermissionId(permissionId).ifPresent(permission -> {
                            permissionsToAssign.add(permission);
                            assignedPermissions.add(PermissionResponseDTO.fromEntity(permission));
                        });
                    }
                    
                    if (permissionsToAssign.isEmpty()) {
                        return ResponseEntity.badRequest().body(
                                PermissionAssignmentResponseDTO.builder()
                                        .entityId(roleId)
                                        .entityType("ROLE")
                                        .success(false)
                                        .message("No valid permissions found to assign")
                                        .build()
                        );
                    }
                    
                    // Assign permissions
                    Role updatedRole = permissionAssignmentService.assignPermissionsToRole(role, permissionsToAssign);
                    
                    return ResponseEntity.status(HttpStatus.OK).body(
                            PermissionAssignmentResponseDTO.builder()
                                    .entityId(updatedRole.getId())
                                    .entityType("ROLE")
                                    .assignedPermissions(assignedPermissions)
                                    .success(true)
                                    .message("Permissions assigned successfully")
                                    .build()
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Remove permissions from a role
     */
    @DeleteMapping("/roles/{roleId}/permissions")
    public ResponseEntity<PermissionAssignmentResponseDTO> removePermissionsFromRole(
            @PathVariable Long roleId,
            @Valid @RequestBody PermissionAssignmentRequestDTO requestDTO) {
        return roleService.findById(roleId)
                .map(role -> {
                    Set<Permission> permissionsToRemove = new HashSet<>();
                    Set<PermissionResponseDTO> removedPermissions = new HashSet<>();
                    
                    // Find all permissions by their IDs
                    for (String permissionId : requestDTO.getPermissionIds()) {
                        permissionService.findByPermissionId(permissionId).ifPresent(permission -> {
                            permissionsToRemove.add(permission);
                            removedPermissions.add(PermissionResponseDTO.fromEntity(permission));
                        });
                    }
                    
                    if (permissionsToRemove.isEmpty()) {
                        return ResponseEntity.badRequest().body(
                                PermissionAssignmentResponseDTO.builder()
                                        .entityId(roleId)
                                        .entityType("ROLE")
                                        .success(false)
                                        .message("No valid permissions found to remove")
                                        .build()
                        );
                    }
                    
                    // Remove permissions
                    Role updatedRole = permissionAssignmentService.removePermissionsFromRole(role, permissionsToRemove);
                    
                    return ResponseEntity.ok(
                            PermissionAssignmentResponseDTO.builder()
                                    .entityId(updatedRole.getId())
                                    .entityType("ROLE")
                                    .removedPermissions(removedPermissions)
                                    .success(true)
                                    .message("Permissions removed successfully")
                                    .build()
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all permissions assigned to a group
     */
    @GetMapping("/groups/{groupId}/permissions")
    public ResponseEntity<List<PermissionResponseDTO>> getGroupPermissions(@PathVariable Long groupId) {
        return groupService.findById(groupId)
                .map(group -> {
                    List<Permission> permissions = permissionAssignmentService.getGroupPermissions(group);
                    List<PermissionResponseDTO> dtos = permissions.stream()
                            .map(PermissionResponseDTO::fromEntity)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(dtos);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Assign permissions to a group
     */
    @PostMapping("/groups/{groupId}/permissions")
    public ResponseEntity<PermissionAssignmentResponseDTO> assignPermissionsToGroup(
            @PathVariable Long groupId,
            @Valid @RequestBody PermissionAssignmentRequestDTO requestDTO) {
        return groupService.findById(groupId)
                .map(group -> {
                    Set<Permission> permissionsToAssign = new HashSet<>();
                    Set<PermissionResponseDTO> assignedPermissions = new HashSet<>();
                    
                    // Find all permissions by their IDs
                    for (String permissionId : requestDTO.getPermissionIds()) {
                        permissionService.findByPermissionId(permissionId).ifPresent(permission -> {
                            permissionsToAssign.add(permission);
                            assignedPermissions.add(PermissionResponseDTO.fromEntity(permission));
                        });
                    }
                    
                    if (permissionsToAssign.isEmpty()) {
                        return ResponseEntity.badRequest().body(
                                PermissionAssignmentResponseDTO.builder()
                                        .entityId(groupId)
                                        .entityType("GROUP")
                                        .success(false)
                                        .message("No valid permissions found to assign")
                                        .build()
                        );
                    }
                    
                    // Assign permissions
                    Group updatedGroup = permissionAssignmentService.assignPermissionsToGroup(group, permissionsToAssign);
                    
                    return ResponseEntity.status(HttpStatus.OK).body(
                            PermissionAssignmentResponseDTO.builder()
                                    .entityId(updatedGroup.getId())
                                    .entityType("GROUP")
                                    .assignedPermissions(assignedPermissions)
                                    .success(true)
                                    .message("Permissions assigned successfully")
                                    .build()
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Remove permissions from a group
     */
    @DeleteMapping("/groups/{groupId}/permissions")
    public ResponseEntity<PermissionAssignmentResponseDTO> removePermissionsFromGroup(
            @PathVariable Long groupId,
            @Valid @RequestBody PermissionAssignmentRequestDTO requestDTO) {
        return groupService.findById(groupId)
                .map(group -> {
                    Set<Permission> permissionsToRemove = new HashSet<>();
                    Set<PermissionResponseDTO> removedPermissions = new HashSet<>();
                    
                    // Find all permissions by their IDs
                    for (String permissionId : requestDTO.getPermissionIds()) {
                        permissionService.findByPermissionId(permissionId).ifPresent(permission -> {
                            permissionsToRemove.add(permission);
                            removedPermissions.add(PermissionResponseDTO.fromEntity(permission));
                        });
                    }
                    
                    if (permissionsToRemove.isEmpty()) {
                        return ResponseEntity.badRequest().body(
                                PermissionAssignmentResponseDTO.builder()
                                        .entityId(groupId)
                                        .entityType("GROUP")
                                        .success(false)
                                        .message("No valid permissions found to remove")
                                        .build()
                        );
                    }
                    
                    // Remove permissions
                    Group updatedGroup = permissionAssignmentService.removePermissionsFromGroup(group, permissionsToRemove);
                    
                    return ResponseEntity.ok(
                            PermissionAssignmentResponseDTO.builder()
                                    .entityId(updatedGroup.getId())
                                    .entityType("GROUP")
                                    .removedPermissions(removedPermissions)
                                    .success(true)
                                    .message("Permissions removed successfully")
                                    .build()
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
