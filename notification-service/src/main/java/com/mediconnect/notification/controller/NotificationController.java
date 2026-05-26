package com.mediconnect.notification.controller;

import com.mediconnect.notification.dto.NotificationDto;
import com.mediconnect.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // POST /api/notifications/send  — send a custom notification
    @PostMapping("/send")
    public ResponseEntity<NotificationDto.NotificationResponse> send(
            @Valid @RequestBody NotificationDto.SendNotificationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationService.sendNotification(request));
    }

    // POST /api/notifications/appointment-confirmation
    @PostMapping("/appointment-confirmation")
    public ResponseEntity<NotificationDto.NotificationResponse> appointmentConfirmation(
            @RequestParam String patientEmail,
            @RequestParam String patientName,
            @RequestParam String doctorName,
            @RequestParam String specialization,
            @RequestParam String date,
            @RequestParam String time,
            @RequestParam String fee,
            @RequestParam String appointmentId) {
        return ResponseEntity.ok(notificationService.sendAppointmentConfirmation(
                patientEmail, patientName, doctorName, specialization, date, time, fee, appointmentId));
    }

    // POST /api/notifications/appointment-reminder
    @PostMapping("/appointment-reminder")
    public ResponseEntity<NotificationDto.NotificationResponse> appointmentReminder(
            @RequestParam String patientEmail,
            @RequestParam String patientName,
            @RequestParam String doctorName,
            @RequestParam String date,
            @RequestParam String time,
            @RequestParam String appointmentId) {
        return ResponseEntity.ok(notificationService.sendAppointmentReminder(
                patientEmail, patientName, doctorName, date, time, appointmentId));
    }

    // POST /api/notifications/welcome
    @PostMapping("/welcome")
    public ResponseEntity<NotificationDto.NotificationResponse> welcome(
            @RequestParam String email,
            @RequestParam String name,
            @RequestParam String role) {
        return ResponseEntity.ok(notificationService.sendWelcomeEmail(email, name, role));
    }

    // GET /api/notifications/email/{email}
    @GetMapping("/email/{email}")
    public ResponseEntity<List<NotificationDto.NotificationResponse>> getByEmail(
            @PathVariable String email) {
        return ResponseEntity.ok(notificationService.getNotificationsByEmail(email));
    }

    // GET /api/notifications/reference/{referenceId}
    @GetMapping("/reference/{referenceId}")
    public ResponseEntity<List<NotificationDto.NotificationResponse>> getByReference(
            @PathVariable String referenceId) {
        return ResponseEntity.ok(notificationService.getNotificationsByReference(referenceId));
    }

    // GET /api/notifications/failed  — admin use
    @GetMapping("/failed")
    public ResponseEntity<List<NotificationDto.NotificationResponse>> getFailed() {
        return ResponseEntity.ok(notificationService.getFailedNotifications());
    }
}
