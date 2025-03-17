package com.apptware.auth.controllers;

import com.apptware.auth.dto.organization.OrganizationRequestDTO;
import com.apptware.auth.dto.organization.OrganizationResponseDTO;
import com.apptware.auth.dto.organization.OrganizationSummaryDTO;
import com.apptware.auth.models.Organization;
import com.apptware.auth.services.OrganizationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor

public class OrganizationController {
    private final OrganizationService organizationService;

    @GetMapping
    public List<OrganizationSummaryDTO> getAllOrganizations() {
        List<Organization> organizations = organizationService.findAll();
        return OrganizationSummaryDTO.fromEntityList(organizations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponseDTO> getOrganizationById(@PathVariable Long id) {
        return organizationService.findById(id)
                .map(org -> ResponseEntity.ok(OrganizationResponseDTO.fromEntity(org)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<OrganizationResponseDTO> getOrganizationByName(@PathVariable String name) {
        return organizationService.findByName(name)
                .map(org -> ResponseEntity.ok(OrganizationResponseDTO.fromEntity(org)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrganizationResponseDTO> createOrganization(@Valid @RequestBody OrganizationRequestDTO requestDTO) {
        if (organizationService.existsByName(requestDTO.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Organization organization = requestDTO.toEntity();
        Organization savedOrganization = organizationService.save(organization);
        return ResponseEntity.status(HttpStatus.CREATED).body(OrganizationResponseDTO.fromEntity(savedOrganization));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponseDTO> updateOrganization(
            @PathVariable Long id,
            @Valid @RequestBody OrganizationRequestDTO requestDTO) {
        // Check if organization exists
        java.util.Optional<Organization> orgOptional = organizationService.findById(id);
        if (!orgOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Organization existingOrg = orgOptional.get();
        
        // Check if name is being changed and if it conflicts
        if (!existingOrg.getName().equals(requestDTO.getName()) 
                && organizationService.existsByName(requestDTO.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        Organization updatedOrg = requestDTO.updateEntity(existingOrg);
        updatedOrg = organizationService.save(updatedOrg);
        return ResponseEntity.ok(OrganizationResponseDTO.fromEntity(updatedOrg));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        if (!organizationService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        organizationService.deleteById(id);
        return ResponseEntity.ok().build();
    }
    
    @Data
    @AllArgsConstructor
    static class ErrorResponse {
        private String message;
    }
}
