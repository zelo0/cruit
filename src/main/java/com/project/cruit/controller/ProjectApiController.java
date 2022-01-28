package com.project.cruit.controller;

import com.project.cruit.entity.Project;
import com.project.cruit.entity.User;
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

@RestController
@RequiredArgsConstructor
public class ProjectApiController {
    private final ProjectService projectService;
    private final UserService userService;

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
}
