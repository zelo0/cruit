package com.project.cruit.domain.notification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.cruit.domain.Question;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "question")
@NoArgsConstructor
public class QuestionNotification extends Notification {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    @JsonIgnore
    private Question question;
}
