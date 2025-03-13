package com.apptware.auth.dto.group;

import com.apptware.auth.dto.organization.OrganizationSummaryDTO;
import com.apptware.auth.models.Group;
import com.apptware.auth.models.Organization;
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
public class GroupResponseDTO {
    private Long id;
    private OrganizationSummaryDTO organization;
    private String name;
    private int permissionsCount;
    private int usersCount;
    
    public static GroupResponseDTO fromEntity(Group group) {
        OrganizationSummaryDTO orgDto = null;
        if (group.getOrganization() != null) {
            Organization org = group.getOrganization();
            orgDto = OrganizationSummaryDTO.builder()
                    .id(org.getId())
                    .name(org.getName())
                    .contactEmail(org.getContactEmail())
                    .build();
        }
        
        return GroupResponseDTO.builder()
                .id(group.getId())
                .organization(orgDto)
                .name(group.getName())
                .permissionsCount(group.getPermissions() != null ? group.getPermissions().size() : 0)
                .usersCount(group.getUsers() != null ? group.getUsers().size() : 0)
                .build();
    }
    
    public static List<GroupResponseDTO> fromEntityList(List<Group> groups) {
        return groups.stream()
                .map(GroupResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
