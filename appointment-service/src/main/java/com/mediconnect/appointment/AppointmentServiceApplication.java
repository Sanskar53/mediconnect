package com.mediconnect.appointment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * SPRING BOOT CONCEPT: @EnableFeignClients
 * This annotation scans the package for interfaces annotated with @FeignClient
 * and creates HTTP client proxies for them automatically.
 * Without this, PatientClient and DoctorClient would not work.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class AppointmentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppointmentServiceApplication.class, args);
    }
}
