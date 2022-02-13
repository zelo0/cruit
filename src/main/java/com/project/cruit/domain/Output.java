package com.project.cruit.domain;

import javax.persistence.*;

@Entity
public class Output {
    @Id
    @GeneratedValue
    @Column(name = "output_id")
    private Long id;

    private String description;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "output")
    private Project project;
}
