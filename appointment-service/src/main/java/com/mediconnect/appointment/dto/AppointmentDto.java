package com.mediconnect.appointment.dto;

import com.mediconnect.appointment.model.Appointment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentDto {

    @Data
    public static class BookAppointmentRequest {
        @NotBlank(message = "Patient ID required")
        private String patientId;

        @NotBlank(message = "Doctor ID required")
        private String doctorId;

        @NotNull(message = "Appointment date required")
        private LocalDate appointmentDate;

        @NotNull(message = "Appointment time required")
        private LocalTime appointmentTime;

        private String reasonForVisit;
    }

    @Data
    public static class UpdateStatusRequest {
        @NotNull(message = "Status required")
        private Appointment.AppointmentStatus status;
        private String notes;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class AppointmentResponse {
        private String id;
        private String patientId;
        private String patientName;
        private String patientEmail;
        private String doctorId;
        private String doctorName;
        private String specialization;
        private LocalDate appointmentDate;
        private LocalTime appointmentTime;
        private String status;
        private String reasonForVisit;
        private String notes;
        private double consultationFee;
        private String createdAt;
    }
}
