package com.project.cruit.dto;

import com.project.cruit.domain.User;
import com.project.cruit.domain.UserStack;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchUserDto {
    private Long id;
    private String name;
    private String profile;
    private String github;
    private String position;
    private List<String> stacks = new ArrayList<>();
    private Boolean canBeLeader;

    public SearchUserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.profile = user.getProfile();
        this.github = user.getGithub();
        this.position = user.getPosition().name();
        this.canBeLeader = user.getCanBeLeader();

        List<UserStack> userStacks = user.getUserStacks();
        for (UserStack userStack : userStacks) {
            stacks.add(userStack.getStack().getImage());
        }
    }
}
