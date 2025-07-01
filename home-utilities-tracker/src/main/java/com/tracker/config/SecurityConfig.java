package com.tracker.config;

import com.tracker.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // 1) Password encoder for your users
    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // Use plain text passwords for development/testing
    }

    // 2) Tell Spring Security to use your UserDetailsService + encoder
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(customUserDetailsService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    // 3) The filter chain: permit /api/auth/** to register/login, but require Basic auth on your usage endpoints
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // plug in your DaoAuthenticationProvider
                .authenticationProvider(authenticationProvider())

                // disable CSRF for simplicity (since you're using a pure API + BasicAuth)
                .csrf(csrf -> csrf.disable())

                // configure URL authorization
                .authorizeHttpRequests(authorize -> authorize
                        // allow anyone to hit your auth endpoints
                        .requestMatchers("/api/auth/**").permitAll()

                        // lock down usage endpoints to authenticated users
                        .requestMatchers(HttpMethod.POST,   "/api/usage/add").authenticated()
                        .requestMatchers(HttpMethod.GET,    "/api/usage/my-usage").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/usage/delete/**").authenticated()

                        // (optionally) allow other URLs through
                        .anyRequest().permitAll()
                )

                // enable HTTP Basic so Postman can send credentials
                .httpBasic(withDefaults());

        return http.build();
    }
}
