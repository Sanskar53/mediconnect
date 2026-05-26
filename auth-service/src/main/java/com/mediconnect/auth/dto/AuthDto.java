package com.mediconnect.auth.dto;

import com.mediconnect.auth.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * SPRING BOOT CONCEPT: DTOs (Data Transfer Objects)
 * We NEVER expose the User model directly to the client.
 * DTOs are simple classes used to:
 *   - Receive data from requests (RegisterRequest, LoginRequest)
 *   - Send data in responses (AuthResponse)
 *
 * This way we control exactly what data comes in and goes out.
 * For example, the password is never returned in AuthResponse.
 */
public class AuthDto {

    /**
     * Used when a new user registers.
     * @NotBlank and @Email are validation annotations.
     * Spring Boot automatically validates these before the method runs.
     */
    @Data
    public static class RegisterRequest {
        @NotBlank(message = "Name is required")
        private String name;

        @NotBlank(message = "Email is required")
        @Email(message = "Must be a valid email address")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        private String password;

        private User.Role role = User.Role.PATIENT;  // Default role is PATIENT
    }

    /**
     * Used when a user logs in.
     */
    @Data
    public static class LoginRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Must be a valid email address")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;
    }

    /**
     * Returned after successful login or register.
     * Contains the JWT token the client will use for future requests.
     */
    @Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class AuthResponse {
        private String token;
        private String refreshToken;
        private String userId;
        private String name;
        private String email;
        private String role;
        private String message;
    }
}
