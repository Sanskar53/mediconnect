package com.mediconnect.doctor.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Document(collection = "doctors")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Doctor {

    @Id
    private String id;

    private String userId;

    private String name;

    @Indexed(unique = true)
    private String email;

    private String phone;

    private String specialization;      // e.g. "Cardiologist", "General Physician"

    private String qualification;       // e.g. "MBBS, MD"

    private String registrationNumber;  // Medical council registration

    private int experienceYears;

    private double consultationFee;     // in rupees

    private List<Availability> availabilitySlots;

    private String about;               // Short bio

    private boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Represents a doctor's availability on a given day.
     * e.g. Monday from 10:00 AM to 1:00 PM, slot duration 30 minutes
     */
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Availability {
        private String dayOfWeek;    // MONDAY, TUESDAY, etc.
        private LocalTime startTime;
        private LocalTime endTime;
        private int slotDurationMinutes;  // e.g. 15 or 30
        private boolean available;
    }
}
