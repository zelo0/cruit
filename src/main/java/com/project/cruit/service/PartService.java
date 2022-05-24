package com.project.cruit.service;

import com.project.cruit.domain.*;
import com.project.cruit.domain.stack.Stack;
import com.project.cruit.domain.status.PartStatus;
import com.project.cruit.domain.part.Part;
import com.project.cruit.dto.DelegateLeaderRequest;
import com.project.cruit.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
