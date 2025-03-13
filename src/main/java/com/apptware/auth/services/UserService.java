package com.apptware.auth.services;

import com.apptware.auth.models.Organization;
import com.apptware.auth.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findByOrganization(Organization organization);
    User save(User user);
    void deleteById(Long id);
    boolean existsByEmail(String email);
    List<User> findAdminUsers();
}
