package com.mediconnect.gateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;

/**
 * SPRING BOOT CONCEPT: @Component
 * Marks this class as a Spring-managed bean.
 * Spring creates one instance and injects it wherever needed via @Autowired.
 *
 * This class validates JWT tokens at the gateway level.
 * If the token is invalid → request is rejected before reaching any service.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")  // reads from application.yml
    private String secret;

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Validates the JWT token.
     * Throws an exception if the token is expired, tampered, or invalid.
     */
    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        try {
            validateToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
