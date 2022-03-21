package com.project.cruit.domain.stack;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.cruit.domain.PartStack;
import com.project.cruit.domain.UserStack;
import lombok.AllArgsConstructor;
import lombok.Getter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@AllArgsConstructor
public class Stack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stack_id")
    private Long id;

    private String name;
    private String image;
    @Column(insertable = false, updatable = false)
    private String dtype;

    @OneToMany(mappedBy = "stack")
    @JsonIgnore
    private List<UserStack> userStacks = new ArrayList<>();

    @OneToMany(mappedBy = "stack")
    @JsonIgnore
    private List<PartStack> partStacks = new ArrayList<>();

    public Stack(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public Stack() {
    }
}
