package com.mediconnect.appointment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "doctor-service")
public interface DoctorClient {

    // Calls GET /api/doctors/{id} on the doctor-service
    @GetMapping("/api/doctors/{id}")
    Map<String, Object> getDoctorById(@PathVariable("id") String doctorId);
}
