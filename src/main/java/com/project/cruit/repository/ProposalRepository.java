package com.project.cruit.repository;

import com.project.cruit.domain.User;
import com.project.cruit.domain.proposal.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findAllBySender(User user);

    List<Proposal> findAllByReceiver(User user);
}
