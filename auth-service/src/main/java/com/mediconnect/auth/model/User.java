package com.mediconnect.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * SPRING BOOT CONCEPT: @Document
 * This annotation maps this Java class to a MongoDB collection.
 * collection = "users" means data is stored in the "users" collection in MongoDB.
 *
 * LOMBOK CONCEPT:
 * @Data       → generates getters, setters, toString, equals, hashCode
 * @Builder    → lets you create objects like: User.builder().name("John").build()
 * @NoArgsConstructor → generates empty constructor (required by MongoDB)
 * @AllArgsConstructor → generates constructor with all fields
 */
@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id  // MongoDB document ID (auto-generated as ObjectId)
    private String id;

    private String name;

    @Indexed(unique = true)  // Creates a unique index — no duplicate emails
    private String email;

    private String password;  // Stored as BCrypt hash, NEVER plain text

    private Role role;  // PATIENT, DOCTOR, or ADMIN

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public enum Role {
        PATIENT, DOCTOR, ADMIN
    }
}
