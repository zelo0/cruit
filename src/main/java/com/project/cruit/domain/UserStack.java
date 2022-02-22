package com.project.cruit.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.cruit.domain.stack.Stack;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
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
    private Stack stack;


    public UserStack() {
    }

    public UserStack(User user, Stack stack) {
        this.user = user;
        this.stack = stack;
    }
}
