package com.project.cruit.domain;

import com.project.cruit.CommonVariables;
import com.project.cruit.domain.notification.Notification;
import com.project.cruit.domain.proposal.Proposal;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private Position position;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserStack> userStacks = new ArrayList<>();

    @Column(length = 10000)
    private String introduction;

    private String profile = CommonVariables.awsS3UrlPrefix + "/profiles/default.png";

    private String github;

    @ElementCollection
    @CollectionTable(
            name = "link",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "url")
    private List<String> links = new ArrayList<>();

    private Double rating;

    @OneToMany(mappedBy = "user")
    private List<UserPart> userParts = new ArrayList<>();

    @OneToMany(mappedBy = "questioner")
    private List<Question> questions = new ArrayList<>();

    private Boolean canBeLeader;

    @OneToMany(mappedBy = "proposer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> proposedProjects = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Proposal> sentProposals = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Proposal> receivedProposals = new ArrayList<>();

    @OneToMany(mappedBy = "subject")
    private List<Notification> notifications = new ArrayList<>();

    public User(String email, String password, String name, String position) {
        this.email = email;
        this.password = password;
        this.name = name;
        switch (position) {
            case "FRONTEND":
                this.position = Position.FRONTEND;
                break;
            case "BACKEND":
                this.position = Position.BACKEND;
                break;
            case "DESIGN":
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
