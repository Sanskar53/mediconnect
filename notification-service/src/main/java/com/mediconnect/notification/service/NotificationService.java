package com.mediconnect.notification.service;

import com.mediconnect.notification.dto.NotificationDto;
import com.mediconnect.notification.model.Notification;
import com.mediconnect.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    /**
     * Sends a generic notification email and saves the record to MongoDB.
     */
    public NotificationDto.NotificationResponse sendNotification(
            NotificationDto.SendNotificationRequest request) {

        Notification notification = Notification.builder()
                .recipientEmail(request.getRecipientEmail())
                .recipientName(request.getRecipientName())
                .subject(request.getSubject())
                .message(request.getMessage())
                .type(request.getType())
                .referenceId(request.getReferenceId())
                .status(Notification.NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        try {
            emailService.sendHtmlEmail(
                    request.getRecipientEmail(),
                    request.getSubject(),
                    request.getMessage()
            );
            notification.setStatus(Notification.NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            log.info("Notification sent to: {}", request.getRecipientEmail());
        } catch (Exception e) {
            notification.setStatus(Notification.NotificationStatus.FAILED);
            notification.setErrorMessage(e.getMessage());
            log.error("Failed to send notification to {}: {}", request.getRecipientEmail(), e.getMessage());
        }

        return mapToResponse(notificationRepository.save(notification));
    }

    /**
     * Sends an appointment confirmation email with a formatted HTML template.
     */
    public NotificationDto.NotificationResponse sendAppointmentConfirmation(
            String patientEmail, String patientName,
            String doctorName, String specialization,
            String date, String time, String fee, String appointmentId) {

        String htmlBody = emailService.buildAppointmentConfirmationEmail(
                patientName, doctorName, specialization, date, time, fee);

        NotificationDto.SendNotificationRequest request = new NotificationDto.SendNotificationRequest();
        request.setRecipientEmail(patientEmail);
        request.setRecipientName(patientName);
        request.setSubject("Appointment Confirmed — Dr. " + doctorName);
        request.setMessage(htmlBody);
        request.setType(Notification.NotificationType.APPOINTMENT_BOOKED);
        request.setReferenceId(appointmentId);

        return sendNotification(request);
    }

    /**
     * Sends an appointment reminder email (to be called by a scheduler 24h before).
     */
    public NotificationDto.NotificationResponse sendAppointmentReminder(
            String patientEmail, String patientName,
            String doctorName, String date, String time, String appointmentId) {

        String htmlBody = emailService.buildReminderEmail(patientName, doctorName, date, time);

        NotificationDto.SendNotificationRequest request = new NotificationDto.SendNotificationRequest();
        request.setRecipientEmail(patientEmail);
        request.setRecipientName(patientName);
        request.setSubject("Reminder: Your appointment is tomorrow");
        request.setMessage(htmlBody);
        request.setType(Notification.NotificationType.APPOINTMENT_REMINDER);
        request.setReferenceId(appointmentId);

        return sendNotification(request);
    }

    /**
     * Sends a welcome email to a newly registered user.
     */
    public NotificationDto.NotificationResponse sendWelcomeEmail(
            String email, String name, String role) {

        String htmlBody = emailService.buildWelcomeEmail(name, role);

        NotificationDto.SendNotificationRequest request = new NotificationDto.SendNotificationRequest();
        request.setRecipientEmail(email);
        request.setRecipientName(name);
        request.setSubject("Welcome to MediConnect 🏥");
        request.setMessage(htmlBody);
        request.setType(Notification.NotificationType.WELCOME);

        return sendNotification(request);
    }

    /**
     * Get all notifications sent to a specific email address.
     */
    public List<NotificationDto.NotificationResponse> getNotificationsByEmail(String email) {
        return notificationRepository.findByRecipientEmailOrderByCreatedAtDesc(email)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Get all notifications linked to a reference (e.g. appointmentId).
     */
    public List<NotificationDto.NotificationResponse> getNotificationsByReference(String referenceId) {
        return notificationRepository.findByReferenceId(referenceId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Get all failed notifications (useful for admin retry logic).
     */
    public List<NotificationDto.NotificationResponse> getFailedNotifications() {
        return notificationRepository.findByStatusOrderByCreatedAtDesc(Notification.NotificationStatus.FAILED)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private NotificationDto.NotificationResponse mapToResponse(Notification n) {
        return NotificationDto.NotificationResponse.builder()
                .id(n.getId())
                .recipientEmail(n.getRecipientEmail())
                .recipientName(n.getRecipientName())
                .subject(n.getSubject())
                .message(n.getMessage())
                .type(n.getType() != null ? n.getType().name() : null)
                .status(n.getStatus().name())
                .referenceId(n.getReferenceId())
                .createdAt(n.getCreatedAt().toString())
                .sentAt(n.getSentAt() != null ? n.getSentAt().toString() : null)
                .build();
    }
}
