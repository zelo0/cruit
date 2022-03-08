package com.project.cruit.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.cruit.domain.notification.QuestionNotification;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue
    @Column(name = "question_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questioner_id")
    @JsonIgnore
    private User questioner;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Question parent;

    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<QuestionNotification> notifications;

    public Question(User questioner, String content, Project project, Question parent) {
        this.questioner = questioner;
        this.content = content;
        this.project = project;
        this.parent = parent;
    }

    public void addChildQuestion(Question childQuestion) {
        this.children.add(childQuestion);
    }
}
