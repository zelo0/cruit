package com.project.cruit.controller;

import com.project.cruit.authentication.CurrentUser;
import com.project.cruit.authentication.SessionUser;
import com.project.cruit.domain.Project;
import com.project.cruit.domain.proposal.DownProposal;
import com.project.cruit.domain.proposal.Proposal;
import com.project.cruit.domain.User;
import com.project.cruit.domain.part.Part;
import com.project.cruit.domain.proposal.TopProposal;
import com.project.cruit.dto.ProposalDto;
import com.project.cruit.dto.ResponseWrapper;
import com.project.cruit.dto.SimpleMessageBody;
//import com.project.cruit.dto.TopProposalDto;
import com.project.cruit.exception.NotHaveSessionException;
import com.project.cruit.service.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/proposals")
public class ProposalApiController {
    private final ProposalService proposalService;
    private final UserService userService;
    private final PartService partService;
    private final ProjectService projectService;
    private final UserPartService userPartService;

    @GetMapping("/me")
    public ResponseWrapper getProposalsInvolvedMe(@CurrentUser SessionUser sessionUser) {
        SessionUser.checkIsNull(sessionUser);



        List<Proposal> sentProposals = proposalService.getSentProposalsOfUser(sessionUser.getId());
        List<Proposal> receivedProposals = proposalService.getReceivedProposalsOfUser(sessionUser.getId());
        return new ResponseWrapper(new GetProposalsInvolvedMe(sentProposals, receivedProposals));
    }

    // 프로젝트 관계자가 유저에게 보내는 프로젝트 함께하자는 요청
    @PostMapping("")
    public ResponseWrapper createProposalToUser(@CurrentUser SessionUser sessionUser,  @RequestBody @Valid CreateProposalToUserRequest request) {
        SessionUser.checkIsNull(sessionUser);


        // 이 파트에 대한 제안을 보낼 수 있는 자격이 있는 지 확인 필요

        User sender = userService.findById(sessionUser.getId());
        User receiver = userService.findByName(request.getReceiverName());
        Part part = partService.findById(request.getPartId());

        proposalService.saveAndMakeNotification(new DownProposal(sender, receiver, part, request.getIsLeaderProposal()),
                "'" + part.getProject().getName() + "' 프로젝트 제안이 들어왔습니다");
        return new ResponseWrapper(new SimpleMessageBody("success"));
    }

    // 유저가 프로젝트 관계자에게 보내는 프로젝트에 참여하고 싶다는 요청
    @PostMapping("/project")
    public ResponseWrapper createProposalToProject(@CurrentUser SessionUser sessionUser,  @RequestBody @Valid CreateProposalToProjectRequest request) {
        SessionUser.checkIsNull(sessionUser);



        User sender = userService.findById(sessionUser.getId());
        Project project = projectService.findById(request.getProjectId());


        Part part = partService.getPart(project, sender.getPosition());
        if (userPartService.hasPartLeader(part)) {
            // 해당 파트에 리더가 있는 경우 리더에게 참여 요청 알림을 보냄
            User partLeader = partService.getLeader(part);
            proposalService.saveAndMakeNotification(new TopProposal(sender, partLeader, part, request.getIsLeaderProposal()),
                    "'" + project.getName() + "' 프로젝트에 참여 요청이 들어왔습니다");
        } else {
            // 해당 파트에 리더가 없는 경우 프로젝트 제안자에게 참여 요청을 보냄
            User proposer = project.getProposer();
            proposalService.saveAndMakeNotification(new TopProposal(sender, proposer, part, request.getIsLeaderProposal()),
                    "'" + project.getName() + "' 프로젝트에 참여 요청이 들어왔습니다");
        }

        return new ResponseWrapper(new SimpleMessageBody("success"));
    }

    @PatchMapping("/approved/{proposalId}")
    public ResponseWrapper setProposalApproved(@PathVariable Long proposalId) {

        Proposal proposal = proposalService.findById(proposalId);
        proposalService.setAcceptedAndAddToMember(proposal);

        return new ResponseWrapper(new SimpleMessageBody("제안이 승인됐습니다"));
    }

    @PatchMapping("/refused/{proposalId}")
    public ResponseWrapper setProposalRefused(@PathVariable Long proposalId) {

        Proposal proposal = proposalService.findById(proposalId);
        proposalService.setRefused(proposal);

        return new ResponseWrapper(new SimpleMessageBody("제안이 거절됐습니다"));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class CreateProposalToUserRequest {
        @NotBlank
        private String receiverName;
        @NotNull
        private Long partId;
        @NotNull
        private Boolean isLeaderProposal;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class CreateProposalToProjectRequest {
        @NotNull
        private Long projectId;
        @NotNull
        private Boolean isLeaderProposal;
    }

    @Data
    static class GetProposalsInvolvedMe {
        private List<ProposalDto> sentProposals;
        private List<ProposalDto> receivedProposals;

        public GetProposalsInvolvedMe(List<Proposal> sentProposals, List<Proposal> receivedProposals) {
            this.sentProposals = sentProposals.stream().map(proposal ->
                            new ProposalDto(proposal.getId(), proposal.getPart().getProject().getId(), proposal.getPart().getProject().getName(), proposal.getPart().getPosition(), proposal.getIsLeaderProposal(), proposal.getStatus().name(),
                                    proposal.getType(),  proposal.getReceiver()))
                    .collect(Collectors.toList());

            this.receivedProposals = receivedProposals.stream().map(proposal ->
                            new ProposalDto(proposal.getId(), proposal.getPart().getProject().getId(), proposal.getPart().getProject().getName(), proposal.getPart().getPosition(), proposal.getIsLeaderProposal(), proposal.getStatus().name(),
                                    proposal.getType(), proposal.getSender()))
                    .collect(Collectors.toList());
        }
    }
}
