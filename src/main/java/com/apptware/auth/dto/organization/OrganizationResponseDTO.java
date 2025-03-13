package com.apptware.auth.dto.organization;

import com.apptware.auth.models.Organization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationResponseDTO {
    private Long id;
    private String name;
    private String contactEmail;
    private LocalDateTime createdAt;
    private int usersCount;
    private int rolesCount;
    private int groupsCount;
    
    public static OrganizationResponseDTO fromEntity(Organization organization) {
        return OrganizationResponseDTO.builder()
                .id(organization.getId())
                .name(organization.getName())
                .contactEmail(organization.getContactEmail())
                .createdAt(organization.getCreatedAt())
                .usersCount(organization.getUsers() != null ? organization.getUsers().size() : 0)
                .rolesCount(organization.getRoles() != null ? organization.getRoles().size() : 0)
                .groupsCount(organization.getGroups() != null ? organization.getGroups().size() : 0)
                .build();
    }
    
    public static List<OrganizationResponseDTO> fromEntityList(List<Organization> organizations) {
        return organizations.stream()
                .map(OrganizationResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
