package com.mediconnect.notification.dto;

import com.mediconnect.notification.model.Notification;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class NotificationDto {

    @Data
    public static class SendNotificationRequest {
        @NotBlank(message = "Recipient email is required")
        @Email(message = "Invalid email address")
        private String recipientEmail;

        @NotBlank(message = "Recipient name is required")
        private String recipientName;

        @NotBlank(message = "Subject is required")
        private String subject;

        @NotBlank(message = "Message is required")
        private String message;

        private Notification.NotificationType type;
        private String referenceId;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class NotificationResponse {
        private String id;
        private String recipientEmail;
        private String recipientName;
        private String subject;
        private String message;
        private String type;
        private String status;
        private String referenceId;
        private String createdAt;
        private String sentAt;
    }
}
