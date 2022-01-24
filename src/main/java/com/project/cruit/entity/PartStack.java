package com.project.cruit.entity;

import com.project.cruit.entity.stack.Stack;

import javax.persistence.*;

@Entity
public class PartStack {
    @Id
    @GeneratedValue
    @Column(name = "part_stack_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id")
    private Part part;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stack_id")
    private Stack stack;
}
