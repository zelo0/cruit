package com.project.cruit.dto;

import com.project.cruit.domain.PartStack;
import com.project.cruit.domain.UserPart;
import com.project.cruit.domain.part.Part;
import com.project.cruit.domain.stack.Stack;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DetailPartDto {
    private Long id;
    private String status;
    private String position;
    private Boolean hasPartLeader;
    private List<Stack> stacks = new ArrayList<>();
    private List<SimpleUserInfo> partMembers = new ArrayList<>();

    public DetailPartDto(Part part) {
        this.id = part.getId();
        this.status = part.getStatus().name();
        this.position = part.getPosition();
        this.hasPartLeader = part.hasPartLeader();

        List<PartStack> partStacks = part.getPartStacks();
        for (PartStack partStack : partStacks) {
            this.stacks.add(partStack.getStack());
        }

        List<UserPart> userParts = part.getUserParts();
        for (UserPart userPart : userParts) {
            this.partMembers.add(new SimpleUserInfo(userPart.getUser(), userPart.getIsLeader()));
        }
    }
}