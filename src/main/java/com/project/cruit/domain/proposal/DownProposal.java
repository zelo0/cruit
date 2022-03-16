package com.project.cruit.domain.proposal;

import com.project.cruit.domain.User;
import com.project.cruit.domain.part.Part;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DOWN")
@NoArgsConstructor
public class DownProposal extends Proposal{
    public DownProposal(User sender, User receiver, Part part, Boolean isLeaderProposal) {
        super(sender, receiver, part, isLeaderProposal);
    }
}
