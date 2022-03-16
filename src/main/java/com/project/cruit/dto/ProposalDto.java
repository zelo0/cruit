package com.project.cruit.dto;

import com.project.cruit.domain.User;
import com.project.cruit.domain.proposal.Proposal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProposalDto {
    private Long id;
    private Long projectId;
    private String projectName;
    private String position;
    private Boolean isLeaderProposal;
    private String status;
    private String type;
    private UserAvatar user;

    public ProposalDto(Long id, Long projectId, String projectName, String position, Boolean isLeaderProposal, String status, String type, User user) {
        this.id = id;
        this.projectId = projectId;
        this.projectName = projectName;
        this.position = position;
        this.isLeaderProposal = isLeaderProposal;
        this.status = status;
        this.type = type;
        this.user = new UserAvatar(user);
    }

    //
//    private ProposalDto(Proposal proposal) {
//        this.id = proposal.getId();
//        this.projectName = proposal.getPart().getProject().getName();
//        this.position = proposal.getPart().getPosition();
//        this.isLeaderProposal = proposal.getIsLeaderProposal();
//        this.status = proposal.getStatus().name();
//    }
}

