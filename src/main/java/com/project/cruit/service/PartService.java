package com.project.cruit.service;

import com.project.cruit.aop.annotation.CheckSessionNotNull;
import com.project.cruit.authentication.SessionUser;
import com.project.cruit.domain.*;
import com.project.cruit.domain.stack.Stack;
import com.project.cruit.domain.status.PartStatus;
import com.project.cruit.domain.part.Part;
import com.project.cruit.dto.DelegateLeaderRequest;
import com.project.cruit.exception.NotPermitException;
import com.project.cruit.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartService {
    private final PartRepository partRepository;
    private final UserService userService;
    private final UserPartService userPartService;
    private final NotificationService notificationService;

    @Transactional
    public void saveParts(List<Part> partList) {
        partRepository.saveAll(partList);
    }

    @Transactional
    public void setStatus(Part part, PartStatus status) {
        part.setStatus(status);
    }

    public Part getBackendPart(Project project) {
        return partRepository.findByProjectAndPosition(project, "BACKEND");
    }
    public Part getFrontendPart(Project project) {
        return partRepository.findByProjectAndPosition(project, "FRONTEND");
    }
    public Part getDesignPart(Project project) {
        return partRepository.findByProjectAndPosition(project, "DESIGN");
    }

    public Part findById(Long id) {
        return partRepository.findById(id).get();
    }

    @Transactional
    public void modifyUsingStacks(Part part, List<Stack> stacks) {
        // 초기화 시킨 뒤 다시 채우기
        part.getPartStacks().clear();
        part.addStacks(stacks);
    }

    @Transactional
    public void modifyUsingStatus(Part part, String status) {
        if (status.equals("RECRUITING")) {
            part.setStatus(PartStatus.RECRUITING);
        } else {
            part.setStatus(PartStatus.COMPLETED);
        }
    }

    @Transactional
    public void deleteMember(Part part, Long memberId) {
        User member = userService.findById(memberId);
        UserPart userPart = userPartService.findByPartAndUser(part, member);
        part.removeMember(userPart, member);
//        part.getUserParts().remove(userPart);
        // 파트에서 추방됐다는 notification 보냄
        notificationService.createNonReferenceNotification(member, "'" + part.getProject().getName() + "'" + "프로젝트에서 추방됐습니다");
    }

    public User getLeader(Part part) {
        return userPartService.findLeaderOfPart(part);
    }

    public Part getPart(Project project, Position position) {
        switch (position) {
            case FRONTEND:
                return getFrontendPart(project);
            case BACKEND:
                return getBackendPart(project);
            default:
                return getDesignPart(project);
        }
    }

    @Transactional
    public void delegateLeader(DelegateLeaderRequest request) {
        UserPart prevLeaderUserPart = userPartService.findLeaderUserPartByPartId(request.getPartId());
        if (prevLeaderUserPart != null) {
            prevLeaderUserPart.setIsLeader(false);
        }
        UserPart nextLeaderUserPart = userPartService.findByPartIdAndUserId(request.getPartId(), request.getNewLeaderId());
        nextLeaderUserPart.setIsLeader(true);
    }

    // 연관된 user 데이터 fetch join
    public Part findByIdWithUsers(Long partId) {
        return partRepository.findByIdWithUsers(partId);
    }

    // 연관된 stack 데이터, project를 fetch join
    public Part findByIdWithProjectAndStacks(Long partId) {
        return partRepository.findByIdWithProjectAndStacks(partId);
    }


    /* 수정 권한이 있는 사용자인지 체크하는 함수 */
    @CheckSessionNotNull
    public void checkModifyingAuthority(SessionUser sessionUser, Part part) {

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

    public List<Part> findPartByPositionInMyProject(String position, Long userId) {
        return partRepository.findPartsByPositionAndProjectProposer(position, userId);
    }

    public List<Part> findPartsByPartLeader(Long userId) {
        return partRepository.findPartsByPartLeader(userId);
    }
}
