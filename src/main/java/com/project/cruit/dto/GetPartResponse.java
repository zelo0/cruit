package com.project.cruit.dto;

import com.project.cruit.domain.PartStack;
import com.project.cruit.domain.UserPart;
import com.project.cruit.domain.part.Part;
import com.project.cruit.domain.stack.Stack;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetPartResponse {
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