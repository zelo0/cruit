package com.project.cruit.controller;

import com.project.cruit.aop.annotation.CheckSessionNotNull;
import com.project.cruit.authentication.CurrentUser;
import com.project.cruit.authentication.SessionUser;
import com.project.cruit.domain.PartStack;
import com.project.cruit.domain.Project;
import com.project.cruit.domain.User;
import com.project.cruit.domain.UserPart;
import com.project.cruit.domain.part.Part;
import com.project.cruit.domain.stack.Stack;
import com.project.cruit.dto.*;
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
import org.hibernate.Session;
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
        // 성능 테스트
//        Part part = partService.findById(partId);

        // fetch join
        // 두 번으로 나눠서 요청하는 이유는 multiple bag exception 때문
        // 여러 연관 리스트를 한꺼번에 fetch join 하는 게 불가
        // 순서가 중요! partStack이 존재하지 않으면 쿼리의 결과는 null
        Part part = partService.findByIdWithUsers(partId);
        partService.findByIdWithProjectAndStacks(partId);

        partService.checkModifyingAuthority(sessionUser, part);

        // selectableStacks (포지션 따라 다름)
        return new ResponseWrapper(new GetPartResponse(part, stackService.findAllByPosition(part.getPosition())));

    }



    @CheckSessionNotNull
    @GetMapping("/involved/{position}")
    public ResponseWrapper<GetInvolvePartsResponse> getInvolvedParts(@CurrentUser SessionUser sessionUser, @PathVariable String position) {

        User targetUser = userService.findById(sessionUser.getId());

//        Set<Part> involvedParts = new HashSet<>();
        Set<PartDto> involvedPartDtos = new HashSet<>();

        // fetch join 쿼리로 튜닝
        // 50개
        // no fetch join -264ms 277ms
        // fetch join - 273ms
        // count 수를 줄여서 52ms (fetch join)

        // 100개 파트의 리더인 경우
        // no fetch join (여러번 쿼리) - 377ms
        // fetch join - 314ms

        // 500개 파트의 리더인 경우
        // no fetch join - 3684ms 4292ms
        // fetch join - 4305ms 4431ms


        // 내가 제안한 프로젝트의 파트 중 position에 맞는 파트
    /*    List<Project> proposedProjects = targetUser.getProposedProjects();
        for (Project proposedProject : proposedProjects) {
            Part targetPart = proposedProject.getParts().stream().filter(part -> part.getPosition().equals(position))
                    .findAny().orElse(null);
            involvedParts.add(targetPart);
        }*/

        // fetch join
        List<Part> partByPositionInMyProject = partService.findPartByPositionInMyProject(position, sessionUser.getId());
        involvedPartDtos.addAll(partByPositionInMyProject.stream()
                .map(involvedPart -> new PartDto(involvedPart, userPartService.hasPartLeader(involvedPart)))
                .collect(Collectors.toList()));

        // 내 포지션이랑 position이 같으면
        /*if (position.equals(targetUser.getPosition().name())) {
            // 내가 파트 리더인 파트들
            List<UserPart> userParts = targetUser.getUserParts();
            for (UserPart userPart : userParts) {
                if (userPart.getIsLeader()) {
                    involvedParts.add(userPart.getPart());
                }
            }
        }*/

        // fetch join
        // 이 경우는 무조건 파트에 리더가 존재
        if (position.equals(targetUser.getPosition().name())) {
            List<Part> partsByPartLeader = partService.findPartsByPartLeader(sessionUser.getId());
            involvedPartDtos.addAll(partsByPartLeader.stream()
                    .map(involvedPart -> new PartDto(involvedPart, true))
                    .collect(Collectors.toList()));
        }




        // 파트마다 count 쿼리를 요청하는 건 비효율적!!!
        // 파트의 리더 존재 유무를 같이 넣음
        /*
        List<PartDto> involvedPartDtos = involvedParts.stream()
                .map(involvedPart -> new PartDto(involvedPart, userPartService.hasPartLeader(involvedPart)))
                .collect(Collectors.toList());
        */

        return new ResponseWrapper(new GetInvolvePartsResponse(involvedPartDtos));
    }

    /* part에서 사용하는 stack 변경 */
    @PatchMapping("/{partId}/stacks")
    public ResponseWrapper modifyStacks(@PathVariable Long partId, @CurrentUser SessionUser sessionUser, @RequestBody @Valid ModifyStackRequest request) {

        Part part = partService.findById(partId);
        partService.checkModifyingAuthority(sessionUser, part);

        partService.modifyUsingStacks(part, request.getStacks());
        return new ResponseWrapper(new SimpleMessageBody("사용하는 스택이 변경됐습니다"));
    }

    /* part의 상태 변경 */
    @PatchMapping("/{partId}/status")
    public ResponseWrapper modifyStatus(@PathVariable Long partId, @CurrentUser SessionUser sessionUser, @RequestBody @Valid ModifyStatusRequest request) {

        Part part = partService.findById(partId);
        partService.checkModifyingAuthority(sessionUser, part);

        partService.modifyUsingStatus(part, request.getStatus());
        return new ResponseWrapper(new SimpleMessageBody("상태가 변경됐습니다"));
    }

    /* part에서 멤버 제거 */
    @DeleteMapping("/{partId}/members/{memberId}")
    public ResponseWrapper deleteMember(@PathVariable Long partId, @PathVariable Long memberId,
                                        @CurrentUser SessionUser sessionUser) {

        Part part = partService.findById(partId);
        partService.checkModifyingAuthority(sessionUser, part);

        // 본인을 지우려고 하면 막기
        if (sessionUser.getId() == memberId) {
            throw new TrialRemovingSelfException("프로젝트에서 본인 자신을 제거할 수 없습니다");
        }

        partService.deleteMember(part, memberId);
        return new ResponseWrapper(new SimpleMessageBody("멤버를 방출했습니다"));
    }

    // part 리더 변경
    @PatchMapping("/leader")
    public ResponseWrapper delegateLeader(@CurrentUser SessionUser sessionUser, @RequestBody @Valid DelegateLeaderRequest request) {

        // 프로젝트 제안자인지 체크


        partService.delegateLeader(request);
        return new ResponseWrapper(new SimpleMessageBody("리더를 변경했습니다"));
    }

}
