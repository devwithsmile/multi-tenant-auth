package com.apptware.auth.dto.user;

import com.apptware.auth.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A simplified User DTO without organization details
 * Used for cases where organization context is already known
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBriefDTO {
    private Long id;
    private String name;
    private String email;
    private boolean isAdmin;
    private int tokenVersion;
    private int permissionsCount;
    private int rolesCount;
    private int groupsCount;
    
    /**
     * Convert a User entity to this DTO
     */
    public static UserBriefDTO fromEntity(User user) {
        return UserBriefDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .isAdmin(user.isAdmin())
                .tokenVersion(user.getTokenVersion() != null ? user.getTokenVersion() : 0)
                .permissionsCount(user.getPermissions() != null ? user.getPermissions().size() : 0)
                .rolesCount(user.getRoles() != null ? user.getRoles().size() : 0)
                .groupsCount(user.getGroups() != null ? user.getGroups().size() : 0)
                .build();
    }
    
    /**
     * Convert a list of User entities to DTOs
     */
    public static List<UserBriefDTO> fromEntityList(List<User> users) {
        return users.stream()
                .map(UserBriefDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
