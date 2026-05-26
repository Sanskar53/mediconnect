package com.mediconnect.patient.service;

import com.mediconnect.patient.dto.PatientDto;
import com.mediconnect.patient.model.Patient;
import com.mediconnect.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientDto.PatientResponse createPatient(PatientDto.CreatePatientRequest request) {
        if (patientRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Patient with this email already exists");
        }

        Patient patient = Patient.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .userId(request.getUserId())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .bloodGroup(request.getBloodGroup())
                .allergies(request.getAllergies())
                .chronicDiseases(request.getChronicDiseases())
                .address(mapAddress(request.getAddress()))
                .emergencyContact(mapEmergencyContact(request.getEmergencyContact()))
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Patient saved = patientRepository.save(patient);
        log.info("Patient created: {}", saved.getId());
        return mapToResponse(saved);
    }

    public PatientDto.PatientResponse getPatientById(String id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        return mapToResponse(patient);
    }

    public PatientDto.PatientResponse getPatientByUserId(String userId) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient not found for userId: " + userId));
        return mapToResponse(patient);
    }

    public List<PatientDto.PatientResponse> getAllPatients() {
        return patientRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PatientDto.PatientResponse> searchPatients(String name) {
        return patientRepository.searchByName(name)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PatientDto.PatientResponse updatePatient(String id, PatientDto.UpdatePatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + id));

        if (request.getName() != null) patient.setName(request.getName());
        if (request.getPhone() != null) patient.setPhone(request.getPhone());
        if (request.getBloodGroup() != null) patient.setBloodGroup(request.getBloodGroup());
        if (request.getAllergies() != null) patient.setAllergies(request.getAllergies());
        if (request.getChronicDiseases() != null) patient.setChronicDiseases(request.getChronicDiseases());
        if (request.getAddress() != null) patient.setAddress(mapAddress(request.getAddress()));
        if (request.getEmergencyContact() != null)
            patient.setEmergencyContact(mapEmergencyContact(request.getEmergencyContact()));

        patient.setUpdatedAt(LocalDateTime.now());
        return mapToResponse(patientRepository.save(patient));
    }

    public void deletePatient(String id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + id));
        patient.setActive(false);  // Soft delete — data is kept, just marked inactive
        patient.setUpdatedAt(LocalDateTime.now());
        patientRepository.save(patient);
        log.info("Patient soft-deleted: {}", id);
    }

    // --- Mapping helpers ---

    private Patient.Address mapAddress(PatientDto.AddressDto dto) {
        if (dto == null) return null;
        return Patient.Address.builder()
                .street(dto.getStreet()).city(dto.getCity())
                .state(dto.getState()).pincode(dto.getPincode())
                .country(dto.getCountry()).build();
    }

    private Patient.EmergencyContact mapEmergencyContact(PatientDto.EmergencyContactDto dto) {
        if (dto == null) return null;
        return Patient.EmergencyContact.builder()
                .name(dto.getName()).relationship(dto.getRelationship())
                .phone(dto.getPhone()).build();
    }

    private PatientDto.PatientResponse mapToResponse(Patient p) {
        PatientDto.AddressDto addressDto = p.getAddress() == null ? null :
                new PatientDto.AddressDto(p.getAddress().getStreet(), p.getAddress().getCity(),
                        p.getAddress().getState(), p.getAddress().getPincode(), p.getAddress().getCountry());

        PatientDto.EmergencyContactDto ecDto = p.getEmergencyContact() == null ? null :
                new PatientDto.EmergencyContactDto(p.getEmergencyContact().getName(),
                        p.getEmergencyContact().getRelationship(), p.getEmergencyContact().getPhone());

        return PatientDto.PatientResponse.builder()
                .id(p.getId()).userId(p.getUserId()).name(p.getName())
                .email(p.getEmail()).phone(p.getPhone()).dateOfBirth(p.getDateOfBirth())
                .gender(p.getGender()).bloodGroup(p.getBloodGroup())
                .allergies(p.getAllergies()).chronicDiseases(p.getChronicDiseases())
                .address(addressDto).emergencyContact(ecDto)
                .active(p.isActive()).createdAt(p.getCreatedAt().toString())
                .build();
    }
}
