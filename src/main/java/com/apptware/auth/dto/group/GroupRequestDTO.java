package com.apptware.auth.dto.group;

import com.apptware.auth.models.Group;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupRequestDTO {
    @NotNull(message = "Organization ID is required")
    private Long organizationId;
    
    @NotBlank(message = "Group name is required")
    private String name;
    
    public Group toEntity() {
        return Group.builder()
                .name(this.name)
                .build();
    }
    
    public Group updateEntity(Group group) {
        group.setName(this.name);
        return group;
    }
}
