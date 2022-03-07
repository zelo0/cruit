package com.project.cruit.service;

import com.project.cruit.domain.Proposal;
import com.project.cruit.domain.User;
import com.project.cruit.domain.part.Part;
import com.project.cruit.repository.PartRepository;
import com.project.cruit.repository.ProposalRepository;
import com.project.cruit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProposalService {
    private final ProposalRepository proposalRepository;


    @Transactional
    public void save(Proposal proposal) {
        proposalRepository.save(proposal);
    }
}
