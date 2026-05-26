package com.mediconnect.notification.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notifications")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Notification {

    @Id
    private String id;

    private String recipientEmail;
    private String recipientName;

    private String subject;
    private String message;

    private NotificationType type;   // APPOINTMENT_BOOKED, APPOINTMENT_REMINDER, PRESCRIPTION_READY, etc.
    private NotificationStatus status; // PENDING, SENT, FAILED

    private String referenceId;      // appointmentId or prescriptionId
    private String errorMessage;     // if FAILED, stores the error

    private LocalDateTime createdAt;
    private LocalDateTime sentAt;

    public enum NotificationType {
        APPOINTMENT_BOOKED,
        APPOINTMENT_REMINDER,
        APPOINTMENT_CANCELLED,
        APPOINTMENT_CONFIRMED,
        PRESCRIPTION_READY,
        WELCOME
    }

    public enum NotificationStatus {
        PENDING, SENT, FAILED
    }
}
