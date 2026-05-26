package com.mediconnect.prescription.service;

import com.mediconnect.prescription.client.DoctorClient;
import com.mediconnect.prescription.client.PatientClient;
import com.mediconnect.prescription.dto.PrescriptionDto;
import com.mediconnect.prescription.model.Prescription;
import com.mediconnect.prescription.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientClient patientClient;
    private final DoctorClient doctorClient;

    public PrescriptionDto.PrescriptionResponse createPrescription(PrescriptionDto.CreatePrescriptionRequest request) {
        // Fetch patient and doctor info from other services
        Map<String, Object> patient = patientClient.getPatientById(request.getPatientId());
        Map<String, Object> doctor = doctorClient.getDoctorById(request.getDoctorId());

        List<Prescription.Medicine> medicines = request.getMedicines().stream()
                .map(m -> Prescription.Medicine.builder()
                        .name(m.getName()).dosage(m.getDosage())
                        .frequency(m.getFrequency()).duration(m.getDuration())
                        .instructions(m.getInstructions()).build())
                .collect(Collectors.toList());

        Prescription prescription = Prescription.builder()
                .appointmentId(request.getAppointmentId())
                .patientId(request.getPatientId())
                .patientName((String) patient.get("name"))
                .patientEmail((String) patient.get("email"))
                .doctorId(request.getDoctorId())
                .doctorName((String) doctor.get("name"))
                .doctorRegistrationNumber((String) doctor.get("registrationNumber"))
                .prescriptionDate(LocalDate.now())
                .diagnosis(request.getDiagnosis())
                .medicines(medicines)
                .advice(request.getAdvice())
                .followUpDate(request.getFollowUpDate())
                .createdAt(LocalDateTime.now())
                .build();

        Prescription saved = prescriptionRepository.save(prescription);
        log.info("Prescription created: {} for patient {}", saved.getId(), saved.getPatientName());
        return mapToResponse(saved);
    }

    public PrescriptionDto.PrescriptionResponse getPrescriptionById(String id) {
        return mapToResponse(prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found: " + id)));
    }

    public PrescriptionDto.PrescriptionResponse getPrescriptionByAppointment(String appointmentId) {
        return mapToResponse(prescriptionRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("No prescription found for appointment: " + appointmentId)));
    }

    public List<PrescriptionDto.PrescriptionResponse> getPatientPrescriptions(String patientId) {
        return prescriptionRepository.findByPatientIdOrderByPrescriptionDateDesc(patientId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<PrescriptionDto.PrescriptionResponse> getDoctorPrescriptions(String doctorId) {
        return prescriptionRepository.findByDoctorIdOrderByPrescriptionDateDesc(doctorId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private PrescriptionDto.PrescriptionResponse mapToResponse(Prescription p) {
        List<PrescriptionDto.MedicineDto> medicineDtos = p.getMedicines().stream()
                .map(m -> new PrescriptionDto.MedicineDto(
                        m.getName(), m.getDosage(), m.getFrequency(),
                        m.getDuration(), m.getInstructions()))
                .collect(Collectors.toList());

        return PrescriptionDto.PrescriptionResponse.builder()
                .id(p.getId()).appointmentId(p.getAppointmentId())
                .patientId(p.getPatientId()).patientName(p.getPatientName())
                .doctorId(p.getDoctorId()).doctorName(p.getDoctorName())
                .doctorRegistrationNumber(p.getDoctorRegistrationNumber())
                .prescriptionDate(p.getPrescriptionDate()).diagnosis(p.getDiagnosis())
                .medicines(medicineDtos).advice(p.getAdvice())
                .followUpDate(p.getFollowUpDate())
                .createdAt(p.getCreatedAt().toString())
                .build();
    }
}
