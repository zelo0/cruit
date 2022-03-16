package com.project.cruit.controller;

import com.project.cruit.domain.notification.Notification;
import com.project.cruit.dto.ResponseWrapper;
import com.project.cruit.dto.SimpleMessageBody;
import com.project.cruit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationApiController {
    private final NotificationService notificationService;

    @PatchMapping("/read/{notificationId}")
    public ResponseWrapper setNotificationRead(@PathVariable Long notificationId) {
        // 적절한 유저의 요청인가?

        Notification notification = notificationService.findById(notificationId);
        notificationService.setRead(notification);
        return new ResponseWrapper(new SimpleMessageBody("알림이 읽음 처리됐습니다"));
    }
}
