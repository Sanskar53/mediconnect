package com.mediconnect.auth.service;

import com.mediconnect.auth.dto.AuthDto;
import com.mediconnect.auth.model.User;
import com.mediconnect.auth.repository.UserRepository;
import com.mediconnect.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * SPRING BOOT CONCEPT: @Service (Business Logic Layer)
 *
 * The Service layer sits between the Controller and Repository.
 * - Controller handles HTTP (request/response)
 * - Service handles BUSINESS LOGIC (rules, validation, processing)
 * - Repository handles DATABASE (queries)
 *
 * This separation makes code clean, testable, and maintainable.
 *
 * @RequiredArgsConstructor (Lombok) generates a constructor for all
 * 'final' fields — this is how Spring injects dependencies (Dependency Injection).
 *
 * @Slf4j (Lombok) gives us a 'log' object for logging.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user (patient, doctor, or admin).
     * Steps:
     * 1. Check if email already exists
     * 2. Hash the password (NEVER store plain text!)
     * 3. Save user to MongoDB
     * 4. Generate and return JWT token
     */
    public AuthDto.AuthResponse register(AuthDto.RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        // Check for duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered: " + request.getEmail());
        }

        // Build the user entity
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))  // BCrypt hash
                .role(request.getRole())
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Save to MongoDB
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        // Generate tokens
        String accessToken = jwtService.generateToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);

        return AuthDto.AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .message("Registration successful")
                .build();
    }

    /**
     * Authenticates an existing user.
     * Steps:
     * 1. Find user by email
     * 2. Verify password matches the stored BCrypt hash
     * 3. Generate and return JWT token
     */
    public AuthDto.AuthResponse login(AuthDto.LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        // Find active user by email
        User user = userRepository.findByEmailAndActive(request.getEmail(), true)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // BCrypt comparison: checks plain text against stored hash
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        log.info("User logged in successfully: {}", user.getId());

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthDto.AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .message("Login successful")
                .build();
    }
}
