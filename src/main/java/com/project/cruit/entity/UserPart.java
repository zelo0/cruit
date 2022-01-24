package com.project.cruit.entity;

import javax.persistence.*;

@Entity
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
}
