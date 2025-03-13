package com.apptware.auth.dto.user;

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
public class UserSummaryDTO {
    private Long id;
    private Long organizationId;
    private String email;
    private boolean isAdmin;
    
    public static UserSummaryDTO fromEntity(User user) {
        return UserSummaryDTO.builder()
                .id(user.getId())
                .organizationId(user.getOrganization() != null ? user.getOrganization().getId() : null)
                .email(user.getEmail())
                .isAdmin(user.isAdmin())
                .build();
    }
    
    public static List<UserSummaryDTO> fromEntityList(List<User> users) {
        return users.stream()
                .map(UserSummaryDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
