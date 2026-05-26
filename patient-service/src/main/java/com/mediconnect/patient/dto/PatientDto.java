package com.mediconnect.patient.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class PatientDto {

    @Data
    public static class CreatePatientRequest {
        @NotBlank(message = "Name is required")
        private String name;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Phone is required")
        @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian phone number")
        private String phone;

        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        private LocalDate dateOfBirth;

        @NotBlank(message = "Gender is required")
        private String gender;

        private String bloodGroup;
        private String userId;

        private AddressDto address;
        private List<String> allergies;
        private List<String> chronicDiseases;
        private EmergencyContactDto emergencyContact;
    }

    @Data
    public static class UpdatePatientRequest {
        private String name;
        private String phone;
        private String bloodGroup;
        private AddressDto address;
        private List<String> allergies;
        private List<String> chronicDiseases;
        private EmergencyContactDto emergencyContact;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientResponse {
        private String id;
        private String userId;
        private String name;
        private String email;
        private String phone;
        private LocalDate dateOfBirth;
        private String gender;
        private String bloodGroup;
        private AddressDto address;
        private List<String> allergies;
        private List<String> chronicDiseases;
        private EmergencyContactDto emergencyContact;
        private boolean active;
        private String createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressDto {
        private String street;
        private String city;
        private String state;
        private String pincode;
        private String country;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmergencyContactDto {
        private String name;
        private String relationship;
        private String phone;
    }
}
