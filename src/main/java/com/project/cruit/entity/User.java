package com.project.cruit.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    private Position position;

    @OneToMany(mappedBy = "user")
    private List<UserStack> userStacks;

    private String introduction;
    private String profile;
    private String github;

    @ElementCollection
    @CollectionTable(
            name = "link",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "url")
    private List<String> links;

    private Double rating;

    @OneToMany(mappedBy = "user")
    private List<UserPart> userParts = new ArrayList<>();

    @OneToMany(mappedBy = "questioner")
    private List<Question> questions = new ArrayList<>();

    private Boolean canBeLeader;


    public User() {
    }

    public User(String email, String password, String name, Position position, String github, Boolean canBeLeader) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.position = position;
        this.github = github;
        this.canBeLeader = canBeLeader;
    }
}
