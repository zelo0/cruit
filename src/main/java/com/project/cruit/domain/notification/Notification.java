package com.project.cruit.domain.notification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.cruit.domain.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Getter @Setter
public class Notification {
    @Id
    @GeneratedValue
    @Column(name = "notification_id")
    private Long id;

    protected String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    @JsonIgnore
    protected User subject;

    @Column(insertable = false, updatable = false, nullable = true)
    private String type;

    private Boolean isRead = false;

    // 참조할 id 없는, 메시지만 있는 notification 생성자
    public Notification(User subject, String message) {
        this.subject = subject;
        this.message = message;
    }

    public Notification() {
    }
}

