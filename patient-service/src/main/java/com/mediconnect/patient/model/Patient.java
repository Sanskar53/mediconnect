package com.mediconnect.patient.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "patients")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    private String id;

    private String userId;      // Reference to auth-service User ID

    private String name;

    @Indexed(unique = true)
    private String email;

    private String phone;

    private LocalDate dateOfBirth;

    private String gender;      // MALE, FEMALE, OTHER

    private String bloodGroup;  // A+, A-, B+, B-, O+, O-, AB+, AB-

    private Address address;

    private List<String> allergies;      // e.g. ["Penicillin", "Peanuts"]

    private List<String> chronicDiseases; // e.g. ["Diabetes", "Hypertension"]

    private EmergencyContact emergencyContact;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * SPRING BOOT CONCEPT: Embedded Documents
     * MongoDB allows nested objects inside a document.
     * Address is stored inside the patient document, not as a separate collection.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private String street;
        private String city;
        private String state;
        private String pincode;
        private String country;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmergencyContact {
        private String name;
        private String relationship;
        private String phone;
    }
}
