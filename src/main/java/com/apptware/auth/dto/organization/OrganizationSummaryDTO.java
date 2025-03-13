package com.apptware.auth.dto.organization;

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
public class OrganizationSummaryDTO {
    private Long id;
    private String name;
    private String contactEmail;
    
    public static OrganizationSummaryDTO fromEntity(Organization organization) {
        return OrganizationSummaryDTO.builder()
                .id(organization.getId())
                .name(organization.getName())
                .contactEmail(organization.getContactEmail())
                .build();
    }
    
    public static List<OrganizationSummaryDTO> fromEntityList(List<Organization> organizations) {
        return organizations.stream()
                .map(OrganizationSummaryDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
