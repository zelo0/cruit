package com.project.cruit.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter @Setter
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
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


    public User(String email, String password, String name, String position) {
        this.email = email;
        this.password = password;
        this.name = name;
        switch (position) {
            case "frontend":
                this.position = Position.FRONTEND;
                break;
            case "backend":
                this.position = Position.BACKEND;
                break;
            case "design":
                this.position = Position.DESIGN;
                break;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getEmail().equals(user.getEmail()) && getName().equals(user.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getName());
    }
}