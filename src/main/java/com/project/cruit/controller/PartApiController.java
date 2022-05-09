package com.project.cruit.controller;

import com.project.cruit.authentication.CurrentUser;
import com.project.cruit.authentication.SessionUser;
import com.project.cruit.domain.PartStack;
import com.project.cruit.domain.Project;
import com.project.cruit.domain.User;
import com.project.cruit.domain.UserPart;
import com.project.cruit.domain.part.Part;
import com.project.cruit.domain.stack.Stack;
import com.project.cruit.dto.ResponseWrapper;
import com.project.cruit.dto.SimpleMessageBody;
import com.project.cruit.dto.SimpleUserInfo;
import com.project.cruit.exception.NotHaveSessionException;
import com.project.cruit.exception.NotPermitException;
import com.project.cruit.exception.TrialRemovingSelfException;
import com.project.cruit.service.PartService;
import com.project.cruit.service.StackService;
import com.project.cruit.service.UserPartService;
import com.project.cruit.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
    private final StackService stackService;

    /* 파트를 수정하기 위해 GET */
    @GetMapping("/{partId}")
    public ResponseWrapper getPart(@CurrentUser SessionUser sessionUser, @PathVariable Long partId) {
        Part part = partService.findById(partId);
        checkModifyingAuthority(sessionUser, part);

        // selectableStacks (포지션 따라 다름)
        return new ResponseWrapper(new GetPartResponse(part, stackService.findAllByPosition(part.getPosition())));

    }

    /* 수정 권한이 있는 사용자인지 체크하는 함수 */
    private void checkModifyingAuthority(SessionUser sessionUser, Part part) {
        SessionUser.checkIsNull(sessionUser);



        // 해당 project의 proposer도 해당 part의 리더도 아니면 권한 X
        List<UserPart> userParts = part.getUserParts();
        String leaderName = null;

        for (UserPart userPart : userParts) {
            if (userPart.getIsLeader()) {
                leaderName = userPart.getUser().getName();
                break;
            }
        }

        if (!sessionUser.getNickname().equals(part.getProject().getProposer().getName()) &&
                !sessionUser.getNickname().equals(leaderName)) {
            throw new NotPermitException("프로젝트의 제안자 또는 파트 리더만 가능합니다");
        }
        //
    }

    @GetMapping("/involved/{position}")
    public ResponseWrapper getInvolvedParts(@CurrentUser SessionUser sessionUser, @PathVariable String position) {
        SessionUser.checkIsNull(sessionUser);


        User targetUser = userService.findById(sessionUser.getId());

        // 내가 제안한 프로젝트의 파트 중 position에 맞는 파트
        Set<Part> involvedParts = new HashSet<>();
        List<Project> proposedProjects = targetUser.getProposedProjects();
        for (Project proposedProject : proposedProjects) {
            Part targetPart = proposedProject.getParts().stream().filter(part -> part.getPosition().equals(position))
                    .findAny().orElse(null);
            involvedParts.add(targetPart);
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

    /* part에서 사용하는 stack 변경 */
    @PatchMapping("/{partId}/stacks")
    public ResponseWrapper modifyStacks(@PathVariable Long partId, @CurrentUser SessionUser sessionUser, @RequestBody @Valid ModifyStackRequest request) {
        Part part = partService.findById(partId);
        checkModifyingAuthority(sessionUser, part);

        partService.modifyUsingStacks(part, request.getStacks());
        return new ResponseWrapper(new SimpleMessageBody("사용하는 스택이 변경됐습니다"));
    }

    /* part의 상태 변경 */
    @PatchMapping("/{partId}/status")
    public ResponseWrapper modifyStatus(@PathVariable Long partId, @CurrentUser SessionUser sessionUser, @RequestBody @Valid ModifyStatusRequest request) {
        Part part = partService.findById(partId);
        checkModifyingAuthority(sessionUser, part);

        partService.modifyUsingStatus(part, request.getStatus());
        return new ResponseWrapper(new SimpleMessageBody("상태가 변경됐습니다"));
    }

    /* part에서 멤버 제거 */
    @DeleteMapping("/{partId}/members/{memberId}")
    public ResponseWrapper deleteMember(@PathVariable Long partId, @PathVariable Long memberId,
                                        @CurrentUser SessionUser sessionUser) {
        Part part = partService.findById(partId);
        checkModifyingAuthority(sessionUser, part);

        // 본인을 지우려고 하면 막기
        if (sessionUser.getId() == memberId) {
            throw new TrialRemovingSelfException("프로젝트에서 본인 자신을 제거할 수 없습니다");
        }

        partService.deleteMember(part, memberId);
        return new ResponseWrapper(new SimpleMessageBody("멤버를 방출했습니다"));
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

    @Data
    static class GetPartResponse {
        private Long id;
        private String position;
        private String status;
        private List<? extends Stack> selectableStacks;
        private List<Stack> usingStacks = new ArrayList<>();
        private List<SimpleUserInfo> members = new ArrayList<>();

        public GetPartResponse(Part part, List<? extends Stack> selectableStacks) {
            id = part.getId();
            position = part.getPosition();
            status = part.getStatus().name();
            this.selectableStacks = selectableStacks;

            List<PartStack> partStacks = part.getPartStacks();
            for (PartStack partStack : partStacks) {
                usingStacks.add(partStack.getStack());
            }

            List<UserPart> userParts = part.getUserParts();
            for (UserPart userPart : userParts) {
                members.add(new SimpleUserInfo(userPart.getUser(), userPart.getIsLeader()));
            }
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class ModifyStackRequest {
        private List<Stack> stacks;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class ModifyStatusRequest {
        @NotBlank
        private String status;
    }

}
