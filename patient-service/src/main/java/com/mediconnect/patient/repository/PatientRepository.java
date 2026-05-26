package com.mediconnect.patient.repository;

import com.mediconnect.patient.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {

    Optional<Patient> findByEmail(String email);

    Optional<Patient> findByUserId(String userId);

    boolean existsByEmail(String email);

    List<Patient> findByActiveTrue();

    // Custom MongoDB query using @Query annotation
    // Searches by name (case-insensitive regex)
    @Query("{ 'name': { $regex: ?0, $options: 'i' }, 'active': true }")
    List<Patient> searchByName(String name);

    // Find patients by city
    @Query("{ 'address.city': ?0, 'active': true }")
    List<Patient> findByCity(String city);

    // Find patients with a specific chronic disease
    @Query("{ 'chronicDiseases': { $in: [?0] }, 'active': true }")
    List<Patient> findByChronicDisease(String disease);
}
