package com.project.cruit.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stack_id")
    @JsonIgnore
    private Stack stack;
}
