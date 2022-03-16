package com.project.cruit.domain.proposal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.cruit.domain.User;
import com.project.cruit.domain.part.Part;
import com.project.cruit.domain.status.ProposalStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@NoArgsConstructor
public class Proposal {
    @Id
    @GeneratedValue
    @Column(name = "proposal_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    @JsonIgnore
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    @JsonIgnore
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id")
    @JsonIgnore
    private Part part;

    // 프로젝트 관계자가 유저에게 보내는 제안인지, 유저가 프로젝트에 보내는 제안인지 구분
    @Column(insertable = false, updatable = false)
    private String type;

    private Boolean isLeaderProposal;

    @Enumerated(EnumType.STRING)
    private ProposalStatus status = ProposalStatus.WAITING;

    public Proposal(User sender, User receiver, Part part, Boolean isLeaderProposal) {
        this.sender = sender;
        this.receiver = receiver;
        this.part = part;
        this.isLeaderProposal = isLeaderProposal;
    }
}
