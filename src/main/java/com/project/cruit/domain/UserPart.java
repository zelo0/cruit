package com.project.cruit.domain;

import com.project.cruit.domain.part.Part;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
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

    public UserPart(User user, Part part) {
        this.user = user;
        this.part = part;
    }

    public UserPart(User user, Part part, Boolean isLeader) {
        this.user = user;
        this.part = part;
        this.isLeader = isLeader;
    }
}
