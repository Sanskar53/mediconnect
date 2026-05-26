package com.mediconnect.notification.repository;

import com.mediconnect.notification.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByRecipientEmailOrderByCreatedAtDesc(String email);
    List<Notification> findByStatusOrderByCreatedAtDesc(Notification.NotificationStatus status);
    List<Notification> findByReferenceId(String referenceId);
}
