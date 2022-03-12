package com.project.cruit.service;

import com.project.cruit.domain.PartStack;
import com.project.cruit.domain.User;
import com.project.cruit.domain.UserPart;
import com.project.cruit.domain.stack.Stack;
import com.project.cruit.domain.status.PartStatus;
import com.project.cruit.domain.Project;
import com.project.cruit.domain.part.Part;
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
        for (Stack stack : stacks) {
            part.getPartStacks().add(new PartStack(part, stack));
        }
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
        UserPart target = userPartService.findByPartAndUser(part, member);
        part.getUserParts().remove(target);
    }
}
