package com.project.cruit.controller;

import com.project.cruit.dto.PageWrapper;
import com.project.cruit.dto.ResponseWrapper;
import com.project.cruit.entity.*;
import com.project.cruit.entity.part.Part;
import com.project.cruit.entity.stack.Stack;
import com.project.cruit.error.InvalidPageOffsetException;
import com.project.cruit.service.PartService;
import com.project.cruit.service.ProjectService;
import com.project.cruit.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ProjectApiController {
    private final ProjectService projectService;
    private final UserService userService;
    private final PartService partService;

    @GetMapping("/api/v1/projects")
    public PageWrapper<ReadProjectResponse> projects(@RequestParam(name = "q", defaultValue = "") String stackFilter,
                                                     @RequestParam(name="offset", defaultValue = "0") int offset,
                                                     @RequestParam(name = "limit", defaultValue = "5") int limit) {
        Page<Project> projects;
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "id"));
        if (stackFilter.isBlank()) {
            projects =projectService.findAll(pageRequest);
        } else {
            List<String> stackFilterList = List.of(stackFilter.split(";"));
            projects = projectService.findByStackFilter(stackFilterList, pageRequest);
        }
        // page offset이 너무 크면 에러
        if (projects.getTotalPages() <= offset) {
            throw new InvalidPageOffsetException();
        }

        List<ReadProjectResponse> responses = projects.stream().map(ReadProjectResponse::new).collect(Collectors.toList());
        return new PageWrapper(responses, projects.hasPrevious(), projects.hasNext(), projects.getTotalPages(), projects.getNumber());
    }

    @PostMapping("/api/v1/projects")
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
    private class ReadProjectResponse {
        private String proposerProfile;
        private String proposerName;
        private PartStatus status;
        private String description;
        private String name;
        private PartDto frontendPart;
        private PartDto backendPart;
        private PartDto designPart;

        public ReadProjectResponse(Project project) {
            proposerProfile = project.getProposer().getProfile();
            proposerName = project.getProposer().getName();
            description = project.getDescription();
            name = project.getName();
            List<Part> parts = project.getParts();
            for (Part part : parts) {
                switch (part.getPosition()) {
                    case "frontend":
                        frontendPart = new PartDto(part);
                        break;
                    case "backend":
                        backendPart = new PartDto(part);
                        break;
                    case "design":
                        designPart = new PartDto(part);
                        break;
                }
            }
        }
    }

    @Data
    static class PartDto {
        private PartStatus status;
        private List<StackDto> stacks = new ArrayList<>();
        private List<UserDto> partMembers = new ArrayList<>();

        public PartDto(Part part) {
            status = part.getStatus();
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
