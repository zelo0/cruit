package com.project.cruit.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.cruit.domain.part.Part;
import com.project.cruit.domain.stack.Stack;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class PartStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "part_stack_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id")
    @JsonIgnore
    private Part part;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stack_id")
    @JsonIgnore
    private Stack stack;

    public PartStack(Part part, Stack stack) {
        this.part = part;
        this.stack = stack;
    }
}
