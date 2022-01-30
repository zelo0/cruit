package com.project.cruit.entity;

import com.project.cruit.entity.part.BackendPart;
import com.project.cruit.entity.part.DesignPart;
import com.project.cruit.entity.part.FrontendPart;
import com.project.cruit.entity.part.Part;
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

    @OneToMany(mappedBy = "project", cascade = CascadeType.PERSIST)
    private List<Part> parts = new ArrayList<>();

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "frontend_part_id")
//    private FrontendPart frontendPart;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "backend_part_id")
//    private BackendPart backendPart;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "design_part_id")
//    private DesignPart designPart;

    public Project() {
    }

    public Project(User proposer, String name, String description) {
        this.proposer = proposer;
        this.name = name;
        this.description = description;
        this.status = ProjectStatus.RECRUITING;
    }

    public void addPart(Part part) {
        parts.add(part);
    }
}
