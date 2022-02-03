package com.project.cruit.entity.part;

import com.project.cruit.entity.*;
import com.project.cruit.entity.stack.Stack;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "position")
@Getter @Setter
public abstract class Part {
    @Id
    @GeneratedValue
    @Column(name = "part_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    private PartStatus status = PartStatus.RECRUITING; // default

    @Column(insertable = false, updatable = false)
    private String position;

    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL)
    private List<PartStack> partStacks = new ArrayList<>();

    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL)
    private List<UserPart> userParts = new ArrayList<>();

    public Part(Project project) {
        this.project = project;
    }

    public Part() {
    }

    public void addMember(User user) {
        userParts.add(new UserPart(user, this));
    }

    public void addStack(Stack stack) {
        partStacks.add(new PartStack(this, stack));}
}
