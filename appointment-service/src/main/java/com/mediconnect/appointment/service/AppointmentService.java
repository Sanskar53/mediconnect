package com.mediconnect.appointment.service;

import com.mediconnect.appointment.client.DoctorClient;
import com.mediconnect.appointment.client.PatientClient;
import com.mediconnect.appointment.dto.AppointmentDto;
import com.mediconnect.appointment.model.Appointment;
import com.mediconnect.appointment.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientClient patientClient;   // Feign — calls patient-service
    private final DoctorClient doctorClient;     // Feign — calls doctor-service

    public AppointmentDto.AppointmentResponse bookAppointment(AppointmentDto.BookAppointmentRequest request) {
        // Check for slot conflict
        if (appointmentRepository.existsByDoctorIdAndAppointmentDateAndAppointmentTime(
                request.getDoctorId(), request.getAppointmentDate(), request.getAppointmentTime())) {
            throw new RuntimeException("This time slot is already booked. Please choose another slot.");
        }

        // Fetch patient info from patient-service via Feign
        Map<String, Object> patient = patientClient.getPatientById(request.getPatientId());

        // Fetch doctor info from doctor-service via Feign
        Map<String, Object> doctor = doctorClient.getDoctorById(request.getDoctorId());

        Appointment appointment = Appointment.builder()
                .patientId(request.getPatientId())
                .patientName((String) patient.get("name"))
                .patientEmail((String) patient.get("email"))
                .patientPhone((String) patient.get("phone"))
                .doctorId(request.getDoctorId())
                .doctorName((String) doctor.get("name"))
                .doctorEmail((String) doctor.get("email"))
                .specialization((String) doctor.get("specialization"))
                .consultationFee(((Number) doctor.get("consultationFee")).doubleValue())
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .reasonForVisit(request.getReasonForVisit())
                .status(Appointment.AppointmentStatus.SCHEDULED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Appointment saved = appointmentRepository.save(appointment);
        log.info("Appointment booked: {} for patient {} with doctor {}",
                saved.getId(), saved.getPatientName(), saved.getDoctorName());

        return mapToResponse(saved);
    }

    public AppointmentDto.AppointmentResponse getAppointmentById(String id) {
        return mapToResponse(appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found: " + id)));
    }

    public List<AppointmentDto.AppointmentResponse> getPatientAppointments(String patientId) {
        return appointmentRepository.findByPatientIdOrderByAppointmentDateDesc(patientId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<AppointmentDto.AppointmentResponse> getDoctorAppointments(String doctorId) {
        return appointmentRepository.findByDoctorIdOrderByAppointmentDateDesc(doctorId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<AppointmentDto.AppointmentResponse> getDoctorAppointmentsByDate(String doctorId, LocalDate date) {
        return appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public AppointmentDto.AppointmentResponse updateStatus(String id, AppointmentDto.UpdateStatusRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found: " + id));
        appointment.setStatus(request.getStatus());
        if (request.getNotes() != null) appointment.setNotes(request.getNotes());
        appointment.setUpdatedAt(LocalDateTime.now());
        return mapToResponse(appointmentRepository.save(appointment));
    }

    public void cancelAppointment(String id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found: " + id));
        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        appointment.setUpdatedAt(LocalDateTime.now());
        appointmentRepository.save(appointment);
    }

    private AppointmentDto.AppointmentResponse mapToResponse(Appointment a) {
        return AppointmentDto.AppointmentResponse.builder()
                .id(a.getId()).patientId(a.getPatientId()).patientName(a.getPatientName())
                .patientEmail(a.getPatientEmail()).doctorId(a.getDoctorId())
                .doctorName(a.getDoctorName()).specialization(a.getSpecialization())
                .appointmentDate(a.getAppointmentDate()).appointmentTime(a.getAppointmentTime())
                .status(a.getStatus().name()).reasonForVisit(a.getReasonForVisit())
                .notes(a.getNotes()).consultationFee(a.getConsultationFee())
                .createdAt(a.getCreatedAt().toString()).build();
    }
}
