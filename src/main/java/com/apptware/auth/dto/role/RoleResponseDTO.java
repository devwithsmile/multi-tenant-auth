package com.apptware.auth.dto.role;

import com.apptware.auth.dto.organization.OrganizationSummaryDTO;
import com.apptware.auth.models.Organization;
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
public class RoleResponseDTO {
    private Long id;
    private OrganizationSummaryDTO organization;
    private String name;
    private int permissionsCount;
    private int usersCount;
    
    public static RoleResponseDTO fromEntity(Role role) {
        OrganizationSummaryDTO orgDto = null;
        if (role.getOrganization() != null) {
            Organization org = role.getOrganization();
            orgDto = OrganizationSummaryDTO.builder()
                    .id(org.getId())
                    .name(org.getName())
                    .contactEmail(org.getContactEmail())
                    .build();
        }
        
        return RoleResponseDTO.builder()
                .id(role.getId())
                .organization(orgDto)
                .name(role.getName())
                .permissionsCount(role.getPermissions() != null ? role.getPermissions().size() : 0)
                .usersCount(role.getUsers() != null ? role.getUsers().size() : 0)
                .build();
    }
    
    public static List<RoleResponseDTO> fromEntityList(List<Role> roles) {
        return roles.stream()
                .map(RoleResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
