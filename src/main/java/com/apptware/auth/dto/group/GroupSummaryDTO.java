package com.apptware.auth.dto.group;

import com.apptware.auth.models.Group;
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
public class GroupSummaryDTO {
    private Long id;
    private Long organizationId;
    private String name;
    
    public static GroupSummaryDTO fromEntity(Group group) {
        return GroupSummaryDTO.builder()
                .id(group.getId())
                .organizationId(group.getOrganization() != null ? group.getOrganization().getId() : null)
                .name(group.getName())
                .build();
    }
    
    public static List<GroupSummaryDTO> fromEntityList(List<Group> groups) {
        return groups.stream()
                .map(GroupSummaryDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
