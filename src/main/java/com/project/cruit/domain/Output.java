package com.project.cruit.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Output {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "output_id")
    private Long id;

    private String description;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "output")
    @JsonIgnore
    private Project project;
}
