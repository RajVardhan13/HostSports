package dev.raj.hostsports.repository;

import dev.raj.hostsports.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    boolean existsByEventKey(String eventKey);

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
}