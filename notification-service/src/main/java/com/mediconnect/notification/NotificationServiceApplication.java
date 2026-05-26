package com.mediconnect.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * SPRING BOOT CONCEPT: @EnableScheduling
 * Enables Spring's scheduled task execution.
 * This allows us to run methods automatically on a schedule,
 * e.g., send appointment reminders every day at 8 AM.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class NotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}
