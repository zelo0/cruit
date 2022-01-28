package com.project.cruit.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Project {
    @Id
    @GeneratedValue
    @Column(name = "project_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposer_id")
    private User proposer;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private String name;
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "output_id")
    private Output output;

    @OneToMany(mappedBy = "project")
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<Part> parts = new ArrayList<>();

    public Project() {
    }

    public Project(User proposer, String name, String description) {
        this.proposer = proposer;
        this.name = name;
        this.description = description;
        this.status = ProjectStatus.RECRUITING;
    }

}
