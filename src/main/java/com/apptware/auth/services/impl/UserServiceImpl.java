package com.apptware.auth.services.impl;

import com.apptware.auth.models.Organization;
import com.apptware.auth.models.User;
import com.apptware.auth.repositories.UserRepository;
import com.apptware.auth.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findByOrganization(Organization organization) {
        return userRepository.findByOrganization(organization);
    }

    @Override
    @Transactional
    public User save(User user) {
        // Hash password if it's not already hashed (new user or password change)
        if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty() && !user.getPasswordHash().startsWith("$2a$")) {
            user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<User> findAdminUsers() {
        return userRepository.findByIsAdminTrue();
    }
}
