package com.mediconnect.auth.repository;

import com.mediconnect.auth.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * SPRING BOOT CONCEPT: Repository (Data Access Layer)
 *
 * MongoRepository<User, String> gives you these methods for FREE:
 *   - save(user)         → insert or update a user
 *   - findById(id)       → find user by ID
 *   - findAll()          → get all users
 *   - delete(user)       → delete a user
 *   - count()            → count all users
 *
 * You can also define custom queries using method names.
 * Spring Boot reads the method name and generates the MongoDB query automatically!
 * Example: findByEmail → MongoDB: { email: "..." }
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    // Spring Boot auto-generates: db.users.findOne({ email: email })
    Optional<User> findByEmail(String email);

    // Spring Boot auto-generates: db.users.findOne({ email: email, active: true })
    Optional<User> findByEmailAndActive(String email, boolean active);

    // Spring Boot auto-generates: db.users.countDocuments({ email: email }) > 0
    boolean existsByEmail(String email);
}
