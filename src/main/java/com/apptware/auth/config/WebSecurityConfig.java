package com.apptware.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * Configure security settings to permit all HTTP requests for this API service
     * since we're only using Spring Security for password encoding,
     * not for authentication/authorization at this moment.
     * 
     * @param http HttpSecurity configuration object
     * @return The security filter chain with appropriate configuration
     * @throws Exception If configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for API endpoints
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().permitAll() // Allow all requests without authentication
            );

        return http.build();
    }
}
