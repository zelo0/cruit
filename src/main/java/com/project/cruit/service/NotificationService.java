package com.project.cruit.service;

import com.project.cruit.domain.User;
import com.project.cruit.domain.notification.Notification;
import com.project.cruit.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notificationRepository;

    /* 읽지 않은 알림 개수 */
    public long getUnReadNotificationCount(User user) {
        return notificationRepository.countBySubjectAndIsRead(user, false);
    }

    /* 읽지 않은 알림 리스트 */
    public List<Notification> getUnReadNotifications(User user) {
        return notificationRepository.findAllBySubjectAndIsRead(user, false);
    }
}
