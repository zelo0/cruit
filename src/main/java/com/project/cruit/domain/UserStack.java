package com.project.cruit.domain;

import com.project.cruit.domain.stack.Stack;

import javax.persistence.*;

@Entity
public class UserStack {
    @Id
    @GeneratedValue
    @Column(name = "user_stack_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stack_id")
    private Stack stack;
}
