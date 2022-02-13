package com.project.cruit.domain;

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
    @GeneratedValue
    @Column(name = "part_stack_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id")
    private Part part;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stack_id")
    private Stack stack;

    public PartStack(Part part, Stack stack) {
        this.part = part;
        this.stack = stack;
    }
}
