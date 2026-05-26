package com.mediconnect.appointment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * SPRING BOOT CONCEPT: Feign Client (Inter-Service Communication)
 *
 * @FeignClient lets one microservice call another as if calling a local method.
 * Spring handles the HTTP request automatically.
 *
 * name = "patient-service" → Eureka looks up the actual IP/port of patient-service.
 * This means if patient-service moves to a different port, this still works!
 *
 * Without Feign you'd need to write:
 *   RestTemplate + hardcoded URL + manual JSON parsing
 *
 * With Feign: just define the interface — Spring does the rest.
 */
@FeignClient(name = "patient-service")
public interface PatientClient {

    // Calls GET /api/patients/{id} on the patient-service
    @GetMapping("/api/patients/{id}")
    Map<String, Object> getPatientById(@PathVariable("id") String patientId);
}
