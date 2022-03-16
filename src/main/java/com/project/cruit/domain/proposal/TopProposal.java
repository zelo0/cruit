package com.project.cruit.domain.proposal;

import com.project.cruit.domain.User;
import com.project.cruit.domain.part.Part;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("TOP")
@NoArgsConstructor
public class TopProposal extends Proposal{
    public TopProposal(User sender, User receiver, Part part, Boolean isLeaderProposal) {
        super(sender, receiver, part, isLeaderProposal);
    }
}
