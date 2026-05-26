package com.mediconnect.prescription.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "prescriptions")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Prescription {

    @Id
    private String id;

    private String appointmentId;

    private String patientId;
    private String patientName;
    private String patientEmail;

    private String doctorId;
    private String doctorName;
    private String doctorRegistrationNumber;

    private LocalDate prescriptionDate;

    private String diagnosis;         // e.g. "Viral Fever"

    private List<Medicine> medicines;

    private String advice;            // e.g. "Rest for 3 days, drink plenty of fluids"

    private LocalDate followUpDate;   // Optional follow-up appointment date

    private LocalDateTime createdAt;

    /**
     * Each medicine entry in the prescription.
     * Stored as an embedded array inside the prescription document.
     */
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Medicine {
        private String name;           // e.g. "Paracetamol 500mg"
        private String dosage;         // e.g. "1 tablet"
        private String frequency;      // e.g. "Twice daily"
        private String duration;       // e.g. "5 days"
        private String instructions;   // e.g. "After food"
    }
}
