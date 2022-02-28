package com.project.cruit.controller;

import com.project.cruit.authentication.CurrentUser;
import com.project.cruit.authentication.SessionUser;
import com.project.cruit.dto.PageWrapper;
import com.project.cruit.domain.*;
import com.project.cruit.domain.part.Part;
import com.project.cruit.domain.stack.Stack;
import com.project.cruit.dto.ResponseWrapper;
import com.project.cruit.exception.InvalidPageOffsetException;
import com.project.cruit.exception.NotHaveSessionException;
import com.project.cruit.service.PartService;
import com.project.cruit.service.ProjectService;
import com.project.cruit.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
public class ProjectApiController {
    private final ProjectService projectService;
    private final UserService userService;

    @GetMapping("")
    public PageWrapper<ReadProjectResponse> projects(@RequestParam(name = "q", defaultValue = "") String stackFilter,
                                                     @RequestParam(name="page", defaultValue = "0") int page,
                                                     @RequestParam(name = "limit", defaultValue = "5") int limit) {
        Page<Project> projects;
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id"));
        if (stackFilter.isBlank()) {
            projects =projectService.findAll(pageRequest);
        } else {
            List<String> stackFilterList = List.of(stackFilter.split(";"));
            projects = projectService.findByStackFilter(stackFilterList, pageRequest);
        }
        // page offset이 너무 크면 에러
        if (projects.getTotalPages() != 0 && projects.getTotalPages() <= page) {
            throw new InvalidPageOffsetException();
        }

        List<ReadProjectResponse> responses = projects.stream().map(ReadProjectResponse::new).collect(Collectors.toList());
        return new PageWrapper(responses, projects.hasPrevious(), projects.hasNext(), projects.getTotalPages(), projects.getNumber());
    }

    @PostMapping("")
    public ResponseWrapper createProject(@CurrentUser SessionUser sessionUser, @RequestBody @Valid CreateProjectRequest request) {
        if (sessionUser == null) {
            throw new NotHaveSessionException();
        }

        User proposer = userService.findById(sessionUser.getId());
        Project project = new Project(proposer, request.getName(), request.getDescription());
        Long projectId = projectService.saveProject(project);
        return new ResponseWrapper(new createProjectResponse(projectId));
    }

    @GetMapping("/{projectId}")
    public ResponseWrapper getProject(@PathVariable Long projectId) {
        Project project = projectService.findById(projectId);
        return new ResponseWrapper(new GetProjectResponse(project));
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class CreateProjectRequest {
        @NotEmpty
        private String name;
        @NotEmpty
        private String description;
    }

    @Data
    private class ReadProjectResponse {
        private Long id;
        private String proposerProfile;
        private String proposerName;
        private PartStatus status;
//        private String description;
        private String name;
        private SimplePartDto frontendPart;
        private SimplePartDto backendPart;
        private SimplePartDto designPart;

        public ReadProjectResponse(Project project) {
            id = project.getId();
            proposerProfile = project.getProposer().getProfile();
            proposerName = project.getProposer().getName();
//            description = project.getDescription();
            name = project.getName();
            List<Part> parts = project.getParts();
            for (Part part : parts) {
                switch (part.getPosition()) {
                    case "frontend":
                        frontendPart = new SimplePartDto(part);
                        break;
                    case "backend":
                        backendPart = new SimplePartDto(part);
                        break;
                    case "design":
                        designPart = new SimplePartDto(part);
                        break;
                }
            }
        }
    }

    @Data
    static class SimplePartDto {
        private PartStatus status;
        private List<StackImage> stacks = new ArrayList<>();
        private List<UserImage> partMembers = new ArrayList<>();

        public SimplePartDto(Part part) {
            status = part.getStatus();
            List<PartStack> partStacks = part.getPartStacks();
            for (PartStack partStack : partStacks) {
                Stack stack = partStack.getStack();
                stacks.add(new StackImage(stack));
            }

            List<UserPart> userPartList = part.getUserParts();
            for (UserPart userPart : userPartList) {
                User user = userPart.getUser();
                partMembers.add(new UserImage(user));
            }
        }
    }

    @Data
    static class DetailPartDto {
        private Part part;
        private List<Stack> stacks = new ArrayList<>();
        private List<SimpleUserInfo> partMembers = new ArrayList<>();

        public DetailPartDto(Part part) {
            this.part = part;
            List<PartStack> partStacks = part.getPartStacks();
            for (PartStack partStack : partStacks) {
                this.stacks.add(partStack.getStack());
            }

            List<UserPart> userParts = part.getUserParts();
            for (UserPart userPart : userParts) {
                this.partMembers.add(new SimpleUserInfo(userPart.getUser()));
            }
        }
    }


    @Data
    static class StackImage {
        private String image;

        public StackImage(Stack stack) {
            image = stack.getImage();
        }
    }

    @Data
    static class UserImage {
        private String profile;

        public UserImage(User user) {
            profile = user.getProfile();
        }
    }

    @Data
    @AllArgsConstructor
    static class createProjectResponse {
        @NotEmpty
        Long projectId;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class GetProjectResponse {
        private Long id;
        private SimpleUserInfo proposer;
        private String name;
        private String description;
        private Output output;
        private List<Question> questions;
        private List<DetailPartDto> parts;

        public GetProjectResponse(Project project) {
            id = project.getId();
            proposer = new SimpleUserInfo(project.getProposer());
            name = project.getName();
            description = project.getDescription();
            questions = project.getQuestions();
            parts = project.getParts().stream().map(DetailPartDto::new).collect(Collectors.toList());
        }
    }

    @Data
    static class SimpleUserInfo {
        private Long id;
        private String name;
        private String profile;

        public SimpleUserInfo(User user) {
            id = user.getId();
            name = user.getName();
            profile = user.getProfile();
        }
    }
}
