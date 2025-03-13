package com.apptware.auth.dto.user;

import com.apptware.auth.dto.organization.OrganizationSummaryDTO;
import com.apptware.auth.models.Organization;
import com.apptware.auth.models.User;
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
public class UserResponseDTO {
    private Long id;
    private OrganizationSummaryDTO organization;
    private String email;
    private boolean isAdmin;
    private int tokenVersion;
    private int permissionsCount;
    private int rolesCount;
    private int groupsCount;
    
    public static UserResponseDTO fromEntity(User user) {
        OrganizationSummaryDTO orgDto = null;
        if (user.getOrganization() != null) {
            Organization org = user.getOrganization();
            orgDto = OrganizationSummaryDTO.builder()
                    .id(org.getId())
                    .name(org.getName())
                    .contactEmail(org.getContactEmail())
                    .build();
        }
        
        return UserResponseDTO.builder()
                .id(user.getId())
                .organization(orgDto)
                .email(user.getEmail())
                .isAdmin(user.isAdmin())
                .tokenVersion(user.getTokenVersion())
                .permissionsCount(user.getPermissions() != null ? user.getPermissions().size() : 0)
                .rolesCount(user.getRoles() != null ? user.getRoles().size() : 0)
                .groupsCount(user.getGroups() != null ? user.getGroups().size() : 0)
                .build();
    }
    
    public static List<UserResponseDTO> fromEntityList(List<User> users) {
        return users.stream()
                .map(UserResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
