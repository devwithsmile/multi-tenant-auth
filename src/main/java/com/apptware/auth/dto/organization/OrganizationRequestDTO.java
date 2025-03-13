package com.apptware.auth.dto.organization;

import com.apptware.auth.models.Organization;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationRequestDTO {
    
    @NotBlank(message = "Organization name is required")
    @Size(min = 2, max = 100, message = "Organization name must be between 2 and 100 characters")
    private String name;
    
    @Email(message = "Contact email must be a valid email address")
    private String contactEmail;
    
    public Organization toEntity() {
        return Organization.builder()
                .name(this.name)
                .contactEmail(this.contactEmail)
                .build();
    }
    
    public Organization updateEntity(Organization organization) {
        organization.setName(this.name);
        organization.setContactEmail(this.contactEmail);
        return organization;
    }
}
