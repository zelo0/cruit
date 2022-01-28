package com.project.cruit.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
public class UserPart {
    @Id
    @GeneratedValue
    @Column(name = "user_part_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id")
    private Part part;

    private Boolean isLeader;

    public UserPart() {
    }

    public UserPart(User user, Part part, Boolean isLeader) {
        this.user = user;
        this.part = part;
        this.isLeader = isLeader;
    }
}
