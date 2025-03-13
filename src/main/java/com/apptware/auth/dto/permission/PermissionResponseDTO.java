package com.apptware.auth.dto.permission;

import com.apptware.auth.models.Permission;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionResponseDTO {
    private Long id;
    
    @JsonProperty("permission_id")
    private String permissionId;
    
    private String name;
    
    @JsonProperty("resources")
    private String[] resources;
    
    private String action;
    private int usersCount;
    private int rolesCount;
    private int groupsCount;
    
    public static PermissionResponseDTO fromEntity(Permission permission) {
        return PermissionResponseDTO.builder()
                .id(permission.getId())
                .permissionId(permission.getPermissionId())
                .name(permission.getName())
                .resources(permission.getResources())
                .action(permission.getAction())
                .usersCount(permission.getUsers() != null ? permission.getUsers().size() : 0)
                .rolesCount(permission.getRoles() != null ? permission.getRoles().size() : 0)
                .groupsCount(permission.getGroups() != null ? permission.getGroups().size() : 0)
                .build();
    }
    
    public static List<PermissionResponseDTO> fromEntityList(List<Permission> permissions) {
        return permissions.stream()
                .map(PermissionResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
