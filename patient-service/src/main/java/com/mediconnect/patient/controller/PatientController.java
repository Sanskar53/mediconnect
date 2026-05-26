package com.mediconnect.patient.controller;

import com.mediconnect.patient.dto.PatientDto;
import com.mediconnect.patient.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    // POST /api/patients
    @PostMapping
    public ResponseEntity<PatientDto.PatientResponse> createPatient(
            @Valid @RequestBody PatientDto.CreatePatientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(patientService.createPatient(request));
    }

    // GET /api/patients/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PatientDto.PatientResponse> getPatient(@PathVariable String id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    // GET /api/patients/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<PatientDto.PatientResponse> getPatientByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(patientService.getPatientByUserId(userId));
    }

    // GET /api/patients
    @GetMapping
    public ResponseEntity<List<PatientDto.PatientResponse>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    // GET /api/patients/search?name=John
    @GetMapping("/search")
    public ResponseEntity<List<PatientDto.PatientResponse>> searchPatients(
            @RequestParam String name) {
        return ResponseEntity.ok(patientService.searchPatients(name));
    }

    // PUT /api/patients/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PatientDto.PatientResponse> updatePatient(
            @PathVariable String id,
            @RequestBody PatientDto.UpdatePatientRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(id, request));
    }

    // DELETE /api/patients/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable String id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok("Patient deactivated successfully");
    }
}
