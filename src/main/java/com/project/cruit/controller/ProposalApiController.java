package com.project.cruit.controller;

import com.project.cruit.authentication.CurrentUser;
import com.project.cruit.authentication.SessionUser;
import com.project.cruit.domain.Proposal;
import com.project.cruit.domain.User;
import com.project.cruit.domain.part.Part;
import com.project.cruit.dto.ResponseWrapper;
import com.project.cruit.dto.SimpleMessageBody;
import com.project.cruit.exception.NotHaveSessionException;
import com.project.cruit.service.PartService;
import com.project.cruit.service.ProposalService;
import com.project.cruit.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/proposals")
public class ProposalApiController {
    private final ProposalService proposalService;
    private final UserService userService;
    private final PartService partService;

    @PostMapping("")
    public ResponseWrapper createProposal(@CurrentUser SessionUser sessionUser,  @RequestBody @Valid CreateProposalRequest request) {
        if (sessionUser == null) {
            throw new NotHaveSessionException();
        }

        // 이 파트에 대한 제안을 보낼 수 있는 자격이 있는 지 확인 필요

        User sender = userService.findById(sessionUser.getId());
        User receiver = userService.findByName(request.getReceiverName());
        Part part = partService.findById(request.getPartId());

        proposalService.save(new Proposal(sender, receiver, part, request.getIsLeaderProposal()));
        return new ResponseWrapper(new SimpleMessageBody("success"));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class CreateProposalRequest {
        @NotBlank
        private String receiverName;
        @NotNull
        private Long partId;
        @NotNull
        private Boolean isLeaderProposal;
    }
}
