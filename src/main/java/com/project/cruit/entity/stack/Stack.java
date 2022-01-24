package com.project.cruit.entity.stack;

import com.project.cruit.entity.PartStack;
import com.project.cruit.entity.UserStack;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Stack {

    @Id
    @GeneratedValue
    @Column(name = "stack_id")
    private Long id;

    private String name;
    private String image;

    @OneToMany(mappedBy = "stack")
    private List<UserStack> userStacks;

    @OneToMany(mappedBy = "stack")
    private List<PartStack> partStacks;

}
