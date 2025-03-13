package com.apptware.auth.dto.permission;

import com.apptware.auth.models.Permission;
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
public class PermissionSummaryDTO {
    private Long id;
    private String name;
    
    public static PermissionSummaryDTO fromEntity(Permission permission) {
        return PermissionSummaryDTO.builder()
                .id(permission.getId())
                .name(permission.getName())
                .build();
    }
    
    public static List<PermissionSummaryDTO> fromEntityList(List<Permission> permissions) {
        return permissions.stream()
                .map(PermissionSummaryDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
