package com.mediconnect.doctor.controller;

import com.mediconnect.doctor.dto.DoctorDto;
import com.mediconnect.doctor.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<DoctorDto.DoctorResponse> create(@Valid @RequestBody DoctorDto.CreateDoctorRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.createDoctor(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDto.DoctorResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<DoctorDto.DoctorResponse> getByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(doctorService.getDoctorByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<DoctorDto.DoctorResponse>> getAll() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/specialization/{spec}")
    public ResponseEntity<List<DoctorDto.DoctorResponse>> getBySpecialization(@PathVariable String spec) {
        return ResponseEntity.ok(doctorService.getDoctorsBySpecialization(spec));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DoctorDto.DoctorResponse>> search(@RequestParam String name) {
        return ResponseEntity.ok(doctorService.searchDoctors(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDto.DoctorResponse> update(@PathVariable String id,
            @RequestBody DoctorDto.UpdateDoctorRequest req) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, req));
    }
}
