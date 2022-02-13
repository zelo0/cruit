package com.project.cruit.domain.stack;

import com.project.cruit.domain.PartStack;
import com.project.cruit.domain.UserStack;
import lombok.Getter;
import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
public abstract class Stack {

    @Id
    @GeneratedValue
    @Column(name = "stack_id")
    private Long id;

    private String name;
    private String image;
    @Column(insertable = false, updatable = false)
    private String dtype;

    @OneToMany(mappedBy = "stack")
    private List<UserStack> userStacks;

    @OneToMany(mappedBy = "stack")
    private List<PartStack> partStacks;

    public Stack(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public Stack() {
    }
}
