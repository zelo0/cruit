package com.project.cruit.dto;

import com.project.cruit.domain.PartStack;
import com.project.cruit.domain.User;
import com.project.cruit.domain.UserPart;
import com.project.cruit.domain.part.Part;
import com.project.cruit.domain.stack.Stack;
import com.project.cruit.domain.status.PartStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SimplePartDto {
    private PartStatus status;
    private List<StackImageDto> stacks = new ArrayList<>();
    private List<UserImageDto> partMembers = new ArrayList<>();

    public SimplePartDto(Part part) {
        status = part.getStatus();
        List<PartStack> partStacks = part.getPartStacks();
        for (PartStack partStack : partStacks) {
            Stack stack = partStack.getStack();
            stacks.add(new StackImageDto(stack));
        }

        List<UserPart> userPartList = part.getUserParts();
        for (UserPart userPart : userPartList) {
            User user = userPart.getUser();
            partMembers.add(new UserImageDto(user));
        }
    }
}
