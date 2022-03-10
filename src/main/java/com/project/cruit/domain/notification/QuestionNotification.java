package com.project.cruit.domain.notification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.cruit.domain.Question;
import com.project.cruit.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@DiscriminatorValue(value = "question")
@NoArgsConstructor
public class QuestionNotification extends Notification {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    @JsonIgnore
    private Question question;

    public QuestionNotification(Question question, User proposer, String message) {
        this.question = question;
        this.setSubject(proposer);
        this.setMessage(message);
    }
}
