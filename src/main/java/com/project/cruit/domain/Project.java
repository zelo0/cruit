package com.project.cruit.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.cruit.domain.part.Part;
import com.project.cruit.domain.status.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Project extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposer_id")
    @JsonIgnore
    private User proposer;


    private String name;

    @Column(length = 10000)
    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status = ProjectStatus.PUBLIC;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "output_id")
    private Output output;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Part> parts = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    public Project() {
    }

    @Builder
    public Project(User proposer, String name, String description) {
        this.proposer = proposer;
        this.name = name;
        this.description = description;
    }

    @Builder
    public Project(Long id, User proposer, String name, String description) {
        this.id = id;
        this.proposer = proposer;
        this.name = name;
        this.description = description;
    }

    public void addPart(Part part) {
        parts.add(part);
    }
}
