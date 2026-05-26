package com.mediconnect.prescription.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class PrescriptionDto {

    @Data
    public static class CreatePrescriptionRequest {
        @NotBlank(message = "Appointment ID is required")
        private String appointmentId;

        @NotBlank(message = "Patient ID is required")
        private String patientId;

        @NotBlank(message = "Doctor ID is required")
        private String doctorId;

        @NotBlank(message = "Diagnosis is required")
        private String diagnosis;

        @NotEmpty(message = "At least one medicine is required")
        private List<MedicineDto> medicines;

        private String advice;

        private LocalDate followUpDate;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class PrescriptionResponse {
        private String id;
        private String appointmentId;
        private String patientId;
        private String patientName;
        private String doctorId;
        private String doctorName;
        private String doctorRegistrationNumber;
        private LocalDate prescriptionDate;
        private String diagnosis;
        private List<MedicineDto> medicines;
        private String advice;
        private LocalDate followUpDate;
        private String createdAt;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class MedicineDto {
        @NotBlank private String name;
        @NotBlank private String dosage;
        @NotBlank private String frequency;
        @NotBlank private String duration;
        private String instructions;
    }
}
