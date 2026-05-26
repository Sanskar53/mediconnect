package com.mediconnect.prescription.repository;

import com.mediconnect.prescription.model.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends MongoRepository<Prescription, String> {
    List<Prescription> findByPatientIdOrderByPrescriptionDateDesc(String patientId);
    List<Prescription> findByDoctorIdOrderByPrescriptionDateDesc(String doctorId);
    Optional<Prescription> findByAppointmentId(String appointmentId);
}
