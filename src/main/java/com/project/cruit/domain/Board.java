package com.project.cruit.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;

    @Column(length = 10000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    @JsonIgnore
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;

    public Board(String title, String content, Project project, User writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.project = project;
    }

    @Builder
    public Board(Long id, String title, String content, User writer, Project project) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.project = project;
    }

    @Builder
    public Board(String title, String content, User writer, Project project) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.project = project;
    }
}
