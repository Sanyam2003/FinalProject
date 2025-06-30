package com.tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    // Allow /api/auth/** for registration/login without authentication
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/usage/all").hasRole("ADMIN")              // Only ADMIN can access all usage
                        .requestMatchers("/api/auth/**").permitAll()                 // Allow register/login without login
                        .requestMatchers("/api/usage/**").permitAll()                // Allow public access to /api/usage/** endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")           // Only ADMIN can access
                        .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN") // USER and ADMIN both can access
                        .anyRequest().authenticated()                                // Everything else requires login
                );
        return http.build();
    }

    // Required for authentication-related operations
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Used to encode passwords (for registration and login check)
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
