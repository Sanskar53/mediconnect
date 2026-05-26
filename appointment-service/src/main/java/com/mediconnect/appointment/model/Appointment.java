package com.mediconnect.appointment.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Document(collection = "appointments")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Appointment {

    @Id
    private String id;

    private String patientId;
    private String patientName;
    private String patientEmail;
    private String patientPhone;

    private String doctorId;
    private String doctorName;
    private String doctorEmail;
    private String specialization;

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    private AppointmentStatus status;  // SCHEDULED, CONFIRMED, CANCELLED, COMPLETED, NO_SHOW

    private String reasonForVisit;

    private String notes;            // Doctor's notes (added after visit)

    private double consultationFee;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum AppointmentStatus {
        SCHEDULED, CONFIRMED, CANCELLED, COMPLETED, NO_SHOW
    }
}
