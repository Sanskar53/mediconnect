package com.mediconnect.doctor.repository;

import com.mediconnect.doctor.model.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends MongoRepository<Doctor, String> {
    Optional<Doctor> findByEmail(String email);
    Optional<Doctor> findByUserId(String userId);
    boolean existsByEmail(String email);

    @Query("{ 'specialization': { $regex: ?0, $options: 'i' }, 'active': true }")
    List<Doctor> findBySpecialization(String specialization);

    @Query("{ 'name': { $regex: ?0, $options: 'i' }, 'active': true }")
    List<Doctor> searchByName(String name);

    List<Doctor> findByActiveTrue();
}
