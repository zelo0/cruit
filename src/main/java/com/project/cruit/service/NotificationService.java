package com.project.cruit.service;

import com.project.cruit.domain.proposal.Proposal;
import com.project.cruit.domain.Question;
import com.project.cruit.domain.User;
import com.project.cruit.domain.notification.Notification;
import com.project.cruit.domain.notification.ProposalNotification;
import com.project.cruit.domain.notification.QuestionNotification;
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

    /* proposalNotification 생성해서 insert */
    @Transactional
    public void createProposalNotification(Proposal proposal, User subject, String message) {
        // notification을 만들고 등록
        notificationRepository.save(new ProposalNotification(proposal, subject, message));
    }

    /* questionNotification 생성해서 insert */
    @Transactional
    public void createQuestionNotification(Question question, User subject, String message) {
        notificationRepository.save(new QuestionNotification(question, subject, message));
    }

    /* 참조할 id가 없는, 메시지만 있는 notification 생성 후 insert */
    public void createNonReferenceNotification(User subject, String message) {
        notificationRepository.save(new Notification(subject, message));
    }

    public Notification findById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    @Transactional
    public void setRead(Notification notification) {
        notification.setIsRead(true);
    }
}
