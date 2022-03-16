package com.project.cruit.service;

import com.project.cruit.domain.Project;
import com.project.cruit.domain.Question;
import com.project.cruit.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final NotificationService notificationService;

    @Transactional
    public void save(Question question) {
        // question 생성
        questionRepository.save(question);
        // notification 생성
        notificationService.createQuestionNotification(question, question.getProject().getProposer(),"내가 제안한 프로젝트에 댓글이 달렸습니다");
    }

    public Question findById(Long questionId) {
        return questionRepository.findById(questionId).get();
    }

    @Transactional
    public void modifyContent(Question question, String content) {
        question.setContent(content);
    }

    @Transactional
    public void delete(Question question) {
        questionRepository.delete(question);
    }

    @Transactional
    public void addChild(Question parentQuestion, Question question) {
        // childQuestion 생성, 추가
        parentQuestion.addChildQuestion(question);
        // notification 생성 - 프로젝트에 댓글이 달렸다는 알림
        notificationService.createQuestionNotification(question, question.getProject().getProposer(),"내가 제안한 프로젝트에 댓글이 달렸습니다");
        // notification 생성 - 내가 쓴 댓글에 댓글이 달렸다는 알림
        notificationService.createQuestionNotification(question, parentQuestion.getQuestioner(), "내가 쓴 댓글에 댓글이 달렸습니다");
    }

    public List<Question> findQuestionsByProjectIdAndParentExists(Project project) {
        return questionRepository.findByProjectIdAndParentExists(project);
    }
}
