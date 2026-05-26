package com.mediconnect.auth.controller;

import com.mediconnect.auth.dto.AuthDto;
import com.mediconnect.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * SPRING BOOT CONCEPT: @RestController (Presentation Layer)
 *
 * @RestController = @Controller + @ResponseBody
 * Every method automatically serializes the return value to JSON.
 *
 * @RequestMapping sets the base URL for all endpoints in this class.
 * So all endpoints here start with /api/auth/
 *
 * ResponseEntity<T> lets you control:
 *   - The response body
 *   - The HTTP status code (200, 201, 400, 401, etc.)
 *   - Response headers
 *
 * @Valid triggers the validation annotations in the DTO (@NotBlank, @Email, etc.)
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/register
     * Registers a new user (patient or doctor).
     * Returns 201 Created with JWT token on success.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthDto.AuthResponse> register(
            @Valid @RequestBody AuthDto.RegisterRequest request) {
        AuthDto.AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * POST /api/auth/login
     * Authenticates a user.
     * Returns 200 OK with JWT token on success.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthDto.AuthResponse> login(
            @Valid @RequestBody AuthDto.LoginRequest request) {
        AuthDto.AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/auth/health
     * Simple health check endpoint.
     * Useful to verify the service is running.
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Auth Service is running");
    }
}
