package com.project.cruit.service;

import com.project.cruit.domain.Proposal;
import com.project.cruit.domain.Question;
import com.project.cruit.domain.User;
import com.project.cruit.domain.notification.Notification;
import com.project.cruit.domain.notification.ProposalNotification;
import com.project.cruit.domain.notification.QuestionNotification;
import com.project.cruit.repository.NotificationRepository;
import com.project.cruit.repository.ProposalRepository;
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
    public void createProposalNotification(Proposal proposal) {
        // notification을 만들고 등록
        notificationRepository.save(new ProposalNotification(proposal, proposal.getReceiver(), "프로젝트 제안이 들어왔습니다"));
    }

    /* questionNotification 생성해서 insert */
    @Transactional
    public void createQuestionNotification(Question question) {
        notificationRepository.save(new QuestionNotification(question, question.getProject().getProposer(), "내가 제안한 프로젝트에 질문이 달렸습니다"));
    }
}
