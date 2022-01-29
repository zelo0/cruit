package com.project.cruit.controller;

import com.project.cruit.entity.*;
import com.project.cruit.entity.stack.Stack;
import com.project.cruit.service.ProjectService;
import com.project.cruit.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ProjectApiController {
    private final ProjectService projectService;
    private final UserService userService;

    @GetMapping("/api/projects")
    public ResponseWrapper<ReadProjectResponse> allProjects() {
        List<Project> projects = projectService.findAll();
        List<ReadProjectResponse> responses = projects.stream().map(ReadProjectResponse::new).collect(Collectors.toList());
        return new ResponseWrapper(responses);
    }

    @PostMapping("/projects")
    public void createProject(@RequestBody @Valid CreateProjectRequest request) {
        User proposer = userService.findById(request.getUserId());
        Project project = new Project(proposer, request.getName(), request.getDescription());
        projectService.saveProject(project);
    }

    @Data
    @AllArgsConstructor
    static class CreateProjectRequest {
        private Long userId;
        private String name;
        private String description;
        private Boolean isLeader;
    }

    @Data
    static class ReadProjectResponse {
        private String proposerProfile;
        private String proposerName;
        private ProjectStatus status;
        private String description;
        private String name;
        private PartDto frontendPart;
        private PartDto backendPart;
        private PartDto designPart;



        public ReadProjectResponse(Project project) {
            proposerProfile = project.getProposer().getProfile();
            proposerName = project.getProposer().getName();
            status = project.getStatus();
            description = project.getDescription();
            name = project.getName();

            List<Part> partList = project.getParts();
            for (Part part : partList) {
                PartDto partDto = new PartDto(part);
                switch (part.getPosition()) {
                    case FRONTEND:
                        frontendPart = partDto;
                        break;
                    case BACKEND:
                        backendPart = partDto;
                        break;
                    case DESIGN:
                        designPart = partDto;
                        break;
                }
            }
        }
    }

    @Data
    static class PartDto {
        private List<StackDto> stacks = new ArrayList<>();
        private List<UserDto> partMembers = new ArrayList<>();

        public PartDto(Part part) {
            List<PartStack> partStacks = part.getPartStacks();
            for (PartStack partStack : partStacks) {
                Stack stack = partStack.getStack();
                stacks.add(new StackDto(stack));
            }

            List<UserPart> userPartList = part.getUserParts();
            for (UserPart userPart : userPartList) {
                User user = userPart.getUser();
                partMembers.add(new UserDto(user));
            }
        }
    }

    @Data
    static class StackDto {
        private String image;

        public StackDto(Stack stack) {
            image = stack.getImage();
        }
    }

    @Data
    static class UserDto {
        private String profile;

        public UserDto(User user) {
            profile = user.getProfile();
        }
    }

    
}
