package com.project.cruit.dto;

import com.project.cruit.domain.Project;
import com.project.cruit.domain.User;
import com.project.cruit.domain.UserPart;
import com.project.cruit.domain.UserStack;
import com.project.cruit.domain.stack.Stack;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class DetailUserDto {
    private Long id;
    private String name;
    private String profile;
    private String github;
    private Boolean canBeLeader;
    private String position;
    private String introduction;
    private List<String> linkList;
    private List<Stack> stackList = new ArrayList<>();
    private List<SimpleProjectInfoDto> projectList;

    public DetailUserDto(User user, List<Project> publicProjectsInvolved) {
        id = user.getId();
        name = user.getName();
        profile = user.getProfile();
        github = user.getGithub();
        canBeLeader = user.getCanBeLeader();
        position = user.getPosition().name();
        introduction = user.getIntroduction();
        linkList = user.getLinks();
        projectList = publicProjectsInvolved.stream().map(SimpleProjectInfoDto::new).collect(Collectors.toList());

        List<UserStack> userStacks = user.getUserStacks();
        for (UserStack userStack : userStacks) {
            stackList.add(userStack.getStack());
        }
    }
}
