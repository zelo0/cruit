package com.project.cruit.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.cruit.domain.part.Part;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class UserPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_part_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id")
    @JsonIgnore
    private Part part;

    // 설정 안 했을 시 기본 값 false
    private Boolean isLeader = false;

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
