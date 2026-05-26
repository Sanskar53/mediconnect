package com.mediconnect.doctor.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

public class DoctorDto {

    @Data
    public static class CreateDoctorRequest {
        @NotBlank private String name;
        @Email @NotBlank private String email;
        @NotBlank private String phone;
        @NotBlank private String specialization;
        @NotBlank private String qualification;
        @NotBlank private String registrationNumber;
        private int experienceYears;
        private double consultationFee;
        private String about;
        private String userId;
        private List<AvailabilityDto> availabilitySlots;
    }

    @Data
    public static class UpdateDoctorRequest {
        private String phone;
        private String about;
        private double consultationFee;
        private List<AvailabilityDto> availabilitySlots;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class DoctorResponse {
        private String id;
        private String userId;
        private String name;
        private String email;
        private String phone;
        private String specialization;
        private String qualification;
        private String registrationNumber;
        private int experienceYears;
        private double consultationFee;
        private String about;
        private List<AvailabilityDto> availabilitySlots;
        private boolean active;
        private String createdAt;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class AvailabilityDto {
        private String dayOfWeek;
        private LocalTime startTime;
        private LocalTime endTime;
        private int slotDurationMinutes;
        private boolean available;
    }
}
