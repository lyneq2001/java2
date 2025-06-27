package com.library.repository;

import com.library.entity.Notification;
import com.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedDateDesc(User user);
    List<Notification> findByUserAndReadFalseOrderByCreatedDateDesc(User user);
    long countByUserAndReadFalse(User user);
}
