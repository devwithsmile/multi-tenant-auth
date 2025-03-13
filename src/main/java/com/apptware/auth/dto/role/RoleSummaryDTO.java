package com.apptware.auth.dto.role;

import com.apptware.auth.models.Role;
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
public class RoleSummaryDTO {
    private Long id;
    private Long organizationId;
    private String name;
    
    public static RoleSummaryDTO fromEntity(Role role) {
        return RoleSummaryDTO.builder()
                .id(role.getId())
                .organizationId(role.getOrganization() != null ? role.getOrganization().getId() : null)
                .name(role.getName())
                .build();
    }
    
    public static List<RoleSummaryDTO> fromEntityList(List<Role> roles) {
        return roles.stream()
                .map(RoleSummaryDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
