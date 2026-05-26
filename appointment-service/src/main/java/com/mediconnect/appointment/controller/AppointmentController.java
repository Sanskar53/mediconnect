package com.mediconnect.appointment.controller;

import com.mediconnect.appointment.dto.AppointmentDto;
import com.mediconnect.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // POST /api/appointments
    @PostMapping
    public ResponseEntity<AppointmentDto.AppointmentResponse> book(
            @Valid @RequestBody AppointmentDto.BookAppointmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.bookAppointment(request));
    }

    // GET /api/appointments/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto.AppointmentResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    // GET /api/appointments/patient/{patientId}
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDto.AppointmentResponse>> getByPatient(@PathVariable String patientId) {
        return ResponseEntity.ok(appointmentService.getPatientAppointments(patientId));
    }

    // GET /api/appointments/doctor/{doctorId}
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDto.AppointmentResponse>> getByDoctor(@PathVariable String doctorId) {
        return ResponseEntity.ok(appointmentService.getDoctorAppointments(doctorId));
    }

    // GET /api/appointments/doctor/{doctorId}/date/2024-01-15
    @GetMapping("/doctor/{doctorId}/date/{date}")
    public ResponseEntity<List<AppointmentDto.AppointmentResponse>> getByDoctorAndDate(
            @PathVariable String doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(appointmentService.getDoctorAppointmentsByDate(doctorId, date));
    }

    // PUT /api/appointments/{id}/status
    @PutMapping("/{id}/status")
    public ResponseEntity<AppointmentDto.AppointmentResponse> updateStatus(
            @PathVariable String id,
            @RequestBody AppointmentDto.UpdateStatusRequest request) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, request));
    }

    // DELETE /api/appointments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancel(@PathVariable String id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok("Appointment cancelled successfully");
    }
}
