package com.apptware.auth.controllers;

import com.apptware.auth.models.Organization;
import com.apptware.auth.models.User;
import com.apptware.auth.services.OrganizationService;
import com.apptware.auth.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class UserController {
    private final UserService userService;
    private final OrganizationService organizationService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.<User>notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.<User>notFound().build());
    }

    @GetMapping("/organization/{orgId}")
    public ResponseEntity<List<User>> getUsersByOrganization(@PathVariable Long orgId) {
        return organizationService.findById(orgId)
                .map(organization -> {
                    List<User> users = userService.findByOrganization(organization);
                    return ResponseEntity.ok(users);
                })
                .orElse(ResponseEntity.<List<User>>notFound().build());
    }

    @GetMapping("/admins")
    public List<User> getAdminUsers() {
        return userService.findAdminUsers();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.<User>status(HttpStatus.CONFLICT).build();
        }
        
        // Validate organization if provided
        if (user.getOrganization() != null && user.getOrganization().getId() != null) {
            Long orgId = user.getOrganization().getId();
            if (!organizationService.findById(orgId).isPresent()) {
                return ResponseEntity.<User>status(HttpStatus.BAD_REQUEST).build();
            }
        }
        
        User savedUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User user) {
        // Check if user exists
        java.util.Optional<User> userOptional = userService.findById(id);
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        User existingUser = userOptional.get();
        
        // Check if email is being changed and if it conflicts
        if (!existingUser.getEmail().equals(user.getEmail()) 
                && userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        // Validate organization if provided
        if (user.getOrganization() != null && user.getOrganization().getId() != null) {
            Long orgId = user.getOrganization().getId();
            if (!organizationService.findById(orgId).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        
        user.setId(id);
        User updatedUser = userService.save(user);
        return ResponseEntity.ok(updatedUser);
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
