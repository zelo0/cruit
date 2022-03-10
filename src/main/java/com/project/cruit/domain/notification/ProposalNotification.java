package com.project.cruit.domain.notification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.cruit.domain.Proposal;
import com.project.cruit.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@DiscriminatorValue(value = "proposal")
@NoArgsConstructor
public class ProposalNotification extends Notification {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_id")
    @JsonIgnore
    private Proposal proposal;

    public ProposalNotification(Proposal proposal, User subject, String message) {
        this.proposal = proposal;
        this.setSubject(subject);
        this.setMessage(message);
    }
}
