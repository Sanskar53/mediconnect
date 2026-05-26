package com.mediconnect.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SPRING BOOT CONCEPT: @Configuration + @Bean
 *
 * @Configuration marks this as a class that defines Spring beans.
 * @Bean marks a method whose return value is registered as a Spring bean.
 *
 * This class configures Spring Security for the Auth Service:
 * - All /api/auth/** endpoints are PUBLIC (no login required)
 * - All other endpoints require authentication
 * - Session is STATELESS (we use JWT, not HTTP sessions)
 * - CSRF disabled (not needed for REST APIs)
 *
 * BCryptPasswordEncoder: hashes passwords with BCrypt algorithm.
 * It's slow by design — makes brute-force attacks harder.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // REST APIs don't need CSRF protection
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // No sessions
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()  // Auth endpoints are public
                .anyRequest().authenticated()
            );

        return http.build();
    }

    /**
     * BCryptPasswordEncoder is the industry standard for hashing passwords.
     * It automatically generates a random salt for each password.
     * strength = 12 means 2^12 iterations (more secure, slightly slower)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
