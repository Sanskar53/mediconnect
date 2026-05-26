package com.mediconnect.doctor.service;

import com.mediconnect.doctor.dto.DoctorDto;
import com.mediconnect.doctor.model.Doctor;
import com.mediconnect.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorDto.DoctorResponse createDoctor(DoctorDto.CreateDoctorRequest request) {
        if (doctorRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Doctor with this email already exists");

        Doctor doctor = Doctor.builder()
                .name(request.getName()).email(request.getEmail())
                .phone(request.getPhone()).userId(request.getUserId())
                .specialization(request.getSpecialization())
                .qualification(request.getQualification())
                .registrationNumber(request.getRegistrationNumber())
                .experienceYears(request.getExperienceYears())
                .consultationFee(request.getConsultationFee())
                .about(request.getAbout())
                .availabilitySlots(mapAvailability(request.getAvailabilitySlots()))
                .active(true).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .build();

        return mapToResponse(doctorRepository.save(doctor));
    }

    public DoctorDto.DoctorResponse getDoctorById(String id) {
        return mapToResponse(doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + id)));
    }

    public DoctorDto.DoctorResponse getDoctorByUserId(String userId) {
        return mapToResponse(doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor not found for userId: " + userId)));
    }

    public List<DoctorDto.DoctorResponse> getAllDoctors() {
        return doctorRepository.findByActiveTrue().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<DoctorDto.DoctorResponse> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<DoctorDto.DoctorResponse> searchDoctors(String name) {
        return doctorRepository.searchByName(name).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public DoctorDto.DoctorResponse updateDoctor(String id, DoctorDto.UpdateDoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + id));
        if (request.getPhone() != null) doctor.setPhone(request.getPhone());
        if (request.getAbout() != null) doctor.setAbout(request.getAbout());
        if (request.getConsultationFee() > 0) doctor.setConsultationFee(request.getConsultationFee());
        if (request.getAvailabilitySlots() != null)
            doctor.setAvailabilitySlots(mapAvailability(request.getAvailabilitySlots()));
        doctor.setUpdatedAt(LocalDateTime.now());
        return mapToResponse(doctorRepository.save(doctor));
    }

    private List<Doctor.Availability> mapAvailability(List<DoctorDto.AvailabilityDto> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(d -> Doctor.Availability.builder()
                .dayOfWeek(d.getDayOfWeek()).startTime(d.getStartTime())
                .endTime(d.getEndTime()).slotDurationMinutes(d.getSlotDurationMinutes())
                .available(d.isAvailable()).build()).collect(Collectors.toList());
    }

    private DoctorDto.DoctorResponse mapToResponse(Doctor d) {
        List<DoctorDto.AvailabilityDto> slots = d.getAvailabilitySlots() == null ? null :
                d.getAvailabilitySlots().stream().map(a -> new DoctorDto.AvailabilityDto(
                        a.getDayOfWeek(), a.getStartTime(), a.getEndTime(),
                        a.getSlotDurationMinutes(), a.isAvailable())).collect(Collectors.toList());

        return DoctorDto.DoctorResponse.builder()
                .id(d.getId()).userId(d.getUserId()).name(d.getName()).email(d.getEmail())
                .phone(d.getPhone()).specialization(d.getSpecialization())
                .qualification(d.getQualification()).registrationNumber(d.getRegistrationNumber())
                .experienceYears(d.getExperienceYears()).consultationFee(d.getConsultationFee())
                .about(d.getAbout()).availabilitySlots(slots).active(d.isActive())
                .createdAt(d.getCreatedAt().toString()).build();
    }
}
