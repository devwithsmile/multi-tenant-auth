package com.apptware.auth.controllers;

import com.apptware.auth.dto.user.UserBriefDTO;
import com.apptware.auth.dto.user.UserRequestDTO;
import com.apptware.auth.dto.user.UserResponseDTO;
import com.apptware.auth.dto.user.UserUpdateDTO;
import com.apptware.auth.models.Organization;
import com.apptware.auth.models.User;
import com.apptware.auth.services.OrganizationService;
import com.apptware.auth.services.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final OrganizationService organizationService;

    @GetMapping
    public List<UserBriefDTO> getAllUsers() {
        List<User> users = userService.findAll();
        return UserBriefDTO.fromEntityList(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(UserResponseDTO.fromEntity(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(user -> ResponseEntity.ok(UserResponseDTO.fromEntity(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/organization/{orgId}")
    public ResponseEntity<List<UserBriefDTO>> getUsersByOrganization(@PathVariable Long orgId) {
        return organizationService.findById(orgId)
                .map(organization -> {
                    List<User> users = userService.findByOrganization(organization);
                    return ResponseEntity.ok(UserBriefDTO.fromEntityList(users));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/admins")
    public List<UserBriefDTO> getAdminUsers() {
        List<User> adminUsers = userService.findAdminUsers();
        return UserBriefDTO.fromEntityList(adminUsers);
    }

    /**
     * Create a new user
     * 
     * @param userRequestDTO The user data to create
     * @return The created user or error response
     */
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        // 1. Validate basic requirements first
        Long organizationId = userRequestDTO.getOrganizationId();
        if (organizationId == null) {
            return badRequestResponse("Organization ID is required");
        }
        
        // 2. Check if organization exists (fundamental check)
        if (organizationService.findById(organizationId).isEmpty()) {
            return badRequestResponse("Organization with ID " + organizationId + " does not exist");
        }
        
        // 3. Check business rules - email uniqueness
        if (userService.existsByEmail(userRequestDTO.getEmail())) {
            return badRequestResponse("Email is already in use");
        }

        // 4. Create and save the user
        try {
            Organization organization = organizationService.findById(organizationId)
                .orElseThrow(() -> new IllegalStateException("Organization not found despite previous check")); 
            
            User newUser = userRequestDTO.toEntity();
            newUser.setOrganization(organization);
            
            // Password hashing is now handled automatically in the UserServiceImpl
            // so we don't need to do anything special here with passwords
            
            User savedUser = userService.save(newUser);
            return createdResponse(savedUser);
        } catch (Exception e) {
            return badRequestResponse("Error creating user: " + e.getMessage());
        }
    }

    /**
     * Creates a response with error details
     * @param message The error message
     * @return ResponseEntity with error details
     */

    private ResponseEntity<?> badRequestResponse(String message) {
        return ResponseEntity.badRequest().body(new ErrorResponse(message));
    }

    private ResponseEntity<UserResponseDTO> createdResponse(User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.fromEntity(user));
    }

    @Data
    @AllArgsConstructor
    static class ErrorResponse {
        private String message;
    }

    /**
     * Update an existing user
     *
     * @param id The ID of the user to update
     * @param userUpdateDTO The user data to update
     * @return The updated user or error response
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        // Check if user exists
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        User existingUser = userOptional.get();
        
        // Check if email is being changed and if it conflicts
        if (!existingUser.getEmail().equals(userUpdateDTO.getEmail()) 
                && userService.existsByEmail(userUpdateDTO.getEmail())) {
            return badRequestResponse("Email is already in use");
        }
        
        // Validate organization if provided
        Long organizationId = userUpdateDTO.getOrganizationId();
        if (organizationId != null) {
            Optional<Organization> organizationOpt = organizationService.findById(organizationId);
            if (organizationOpt.isEmpty()) {
                return badRequestResponse("Organization with ID " + organizationId + " does not exist");
            }
            
            // Update organization
            existingUser.setOrganization(organizationOpt.get());
        }
        
        // Update basic fields
        existingUser.setId(id); // Ensure ID doesn't change
        existingUser.setName(userUpdateDTO.getName());
        existingUser.setEmail(userUpdateDTO.getEmail());
        
        // Only update password if provided
        if (userUpdateDTO.getPasswordHash() != null && !userUpdateDTO.getPasswordHash().isEmpty()) {
            existingUser.setPasswordHash(userUpdateDTO.getPasswordHash());
        }
        
        // Update other fields if provided
        if (userUpdateDTO.getIsAdmin() != null) {
            existingUser.setAdmin(userUpdateDTO.getIsAdmin());
        }
        
        if (userUpdateDTO.getIsActive() != null) {
            existingUser.setActive(userUpdateDTO.getIsActive());
        }
        
        if (userUpdateDTO.getReportingManagerId() != null) {
            existingUser.setReportingManagerId(userUpdateDTO.getReportingManagerId());
        }
        
        // Note: Handling of roles, groups and permissions should be done through separate endpoints
        // for better control and security, but we could implement it here if needed
        
        User updatedUser = userService.save(existingUser);
        return ResponseEntity.ok(UserResponseDTO.fromEntity(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userService.findById(id)
                .map(user -> {
                    userService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.<Void>notFound().build());
    }
}
