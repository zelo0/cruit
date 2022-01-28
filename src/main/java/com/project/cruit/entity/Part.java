package com.project.cruit.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Part {
    @Id
    @GeneratedValue
    @Column(name = "part_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    private Position position;


    @OneToMany(mappedBy = "part")
    private List<PartStack> partStacks = new ArrayList<>();

    @OneToMany(mappedBy = "part")
    private List<UserPart> userParts = new ArrayList<>();

    public Part(Project project, Position position) {
        this.project = project;
        this.position = position;
    }
}
