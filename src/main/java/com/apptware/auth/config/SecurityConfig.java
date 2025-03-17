package com.apptware.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

@Configuration
public class SecurityConfig {

    /**
     * Creates a BCrypt password encoder with random salt strength.
     * BCrypt internally handles salt generation.
     * The log rounds parameter determines the complexity - higher is slower but more secure.
     * We randomize between 10-13 rounds to make it difficult for attackers to determine
     * the exact computational cost required for cracking passwords.
     *
     * @return A configured BCryptPasswordEncoder with randomized strength
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Generate a random strength between 10 (minimum recommended) and 13
        // This adds computational complexity variability between users
        int strength = 10 + new SecureRandom().nextInt(4); // Results in 10, 11, 12, or 13
        return new BCryptPasswordEncoder(strength, new SecureRandom());
    }
}
