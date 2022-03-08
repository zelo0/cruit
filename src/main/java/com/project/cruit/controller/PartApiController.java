package com.project.cruit.controller;

import com.project.cruit.authentication.CurrentUser;
import com.project.cruit.authentication.SessionUser;
import com.project.cruit.domain.Project;
import com.project.cruit.domain.User;
import com.project.cruit.domain.UserPart;
import com.project.cruit.domain.part.Part;
import com.project.cruit.dto.ResponseWrapper;
import com.project.cruit.exception.NotHaveSessionException;
import com.project.cruit.service.PartService;
import com.project.cruit.service.UserPartService;
import com.project.cruit.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/parts")
public class PartApiController {
    private final PartService partService;
    private final UserService userService;
    private final UserPartService userPartService;

    @GetMapping("/involved")
    private ResponseWrapper getInvolvedParts(@CurrentUser SessionUser sessionUser) {
        if (sessionUser == null) {
            throw new NotHaveSessionException();
        }


        User targetUser = userService.findById(sessionUser.getId());

        // 내가 제안한 프로젝트의 파트 전부
        Set<Part> involvedParts = new HashSet<>();
        List<Project> proposedProjects = targetUser.getProposedProjects();
        for (Project proposedProject : proposedProjects) {
            involvedParts.addAll(proposedProject.getParts());
        }

        // 내가 파트 리더인 파트들
        List<UserPart> userParts = targetUser.getUserParts();
        for (UserPart userPart : userParts) {
            if (userPart.getIsLeader()) {
                involvedParts.add(userPart.getPart());
            }
        }


        // 파트의 리더 존재 유무를 같이 넣음
        List<PartDto> involvedPartDtos = involvedParts.stream()
                .map(involvedPart -> new PartDto(involvedPart, userPartService.hasPartLeader(involvedPart)))
                .collect(Collectors.toList());

        return new ResponseWrapper(new GetInvolvePartsResponse(involvedPartDtos));
    }

    @Data
    @AllArgsConstructor
    static class GetInvolvePartsResponse {
        private List<PartDto> involvedParts;
    }

    @Data
    @AllArgsConstructor
    static class PartDto {
        private Long projectId;
        private String projectName;
        private String status;
        private Long id;
        private String position;
        private Boolean hasLeader;

        public PartDto(Part part, Boolean hasLeader) {
            this.projectId = part.getProject().getId();
            this.projectName = part.getProject().getName();
            this.status = part.getStatus().name();
            this.id = part.getId();
            this.position = part.getPosition();
            this.hasLeader = hasLeader;
        }
    }
}
