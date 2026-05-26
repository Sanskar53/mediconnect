package com.mediconnect.appointment.repository;

import com.mediconnect.appointment.model.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    List<Appointment> findByPatientIdOrderByAppointmentDateDesc(String patientId);
    List<Appointment> findByDoctorIdOrderByAppointmentDateDesc(String doctorId);
    List<Appointment> findByDoctorIdAndAppointmentDate(String doctorId, LocalDate date);
    List<Appointment> findByStatus(Appointment.AppointmentStatus status);
    boolean existsByDoctorIdAndAppointmentDateAndAppointmentTime(
            String doctorId, LocalDate date, java.time.LocalTime time);
}
