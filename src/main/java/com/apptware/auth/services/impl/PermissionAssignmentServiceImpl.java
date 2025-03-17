package com.apptware.auth.services.impl;

import com.apptware.auth.models.Group;
import com.apptware.auth.models.Permission;
import com.apptware.auth.models.Role;
import com.apptware.auth.models.User;
import com.apptware.auth.repositories.GroupRepository;

import com.apptware.auth.repositories.RoleRepository;
import com.apptware.auth.repositories.UserRepository;
import com.apptware.auth.services.PermissionAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PermissionAssignmentServiceImpl implements PermissionAssignmentService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;
    // PermissionRepository not currently needed but may be used in future extensions
    // private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public User assignPermissionsToUser(User user, Set<Permission> permissions) {
        for (Permission permission : permissions) {
            permission.assignToUser(user);
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User removePermissionsFromUser(User user, Set<Permission> permissions) {
        for (Permission permission : permissions) {
            permission.removeFromUser(user);
        }
        return userRepository.save(user);
    }

    @Override
    public List<Permission> getUserPermissions(User user) {
        if (user.getPermissions() == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(user.getPermissions());
    }

    @Override
    public List<Permission> getUserEffectivePermissions(User user) {
        // Use a set to avoid duplicates
        Set<Permission> allPermissions = new HashSet<>();
        
        // Add direct user permissions
        if (user.getPermissions() != null) {
            allPermissions.addAll(user.getPermissions());
        }
        
        // Add permissions from roles
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                if (role.getPermissions() != null) {
                    allPermissions.addAll(role.getPermissions());
                }
            }
        }
        
        // Add permissions from groups
        if (user.getGroups() != null) {
            for (Group group : user.getGroups()) {
                if (group.getPermissions() != null) {
                    allPermissions.addAll(group.getPermissions());
                }
            }
        }
        
        return new ArrayList<>(allPermissions);
    }

    @Override
    @Transactional
    public Role assignPermissionsToRole(Role role, Set<Permission> permissions) {
        for (Permission permission : permissions) {
            permission.assignToRole(role);
        }
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role removePermissionsFromRole(Role role, Set<Permission> permissions) {
        for (Permission permission : permissions) {
            permission.removeFromRole(role);
        }
        return roleRepository.save(role);
    }

    @Override
    public List<Permission> getRolePermissions(Role role) {
        if (role.getPermissions() == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(role.getPermissions());
    }

    @Override
    @Transactional
    public Group assignPermissionsToGroup(Group group, Set<Permission> permissions) {
        for (Permission permission : permissions) {
            permission.assignToGroup(group);
        }
        return groupRepository.save(group);
    }

    @Override
    @Transactional
    public Group removePermissionsFromGroup(Group group, Set<Permission> permissions) {
        for (Permission permission : permissions) {
            permission.removeFromGroup(group);
        }
        return groupRepository.save(group);
    }

    @Override
    public List<Permission> getGroupPermissions(Group group) {
        if (group.getPermissions() == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(group.getPermissions());
    }

    @Override
    public boolean userHasPermission(User user, String permissionId) {
        return user.hasPermission(permissionId);
    }
}
