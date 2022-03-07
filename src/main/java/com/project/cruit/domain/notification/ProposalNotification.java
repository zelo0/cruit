package com.project.cruit.domain.notification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.cruit.domain.Proposal;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "proposal")
@NoArgsConstructor
public class ProposalNotification extends Notification {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_id")
    @JsonIgnore
    private Proposal proposal;
}
