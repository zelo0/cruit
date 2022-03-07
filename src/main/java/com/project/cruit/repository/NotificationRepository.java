package com.project.cruit.repository;

import com.project.cruit.domain.User;
import com.project.cruit.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    long countBySubjectAndIsRead(User user, Boolean isRead);

    List<Notification> findAllBySubjectAndIsRead(User user, boolean b);
}
