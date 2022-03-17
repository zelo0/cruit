package com.project.cruit.dto;

import com.project.cruit.domain.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GetMyProjectsResponse {
    private List<SimpleProjectDto> projects;

    public GetMyProjectsResponse(List<Project> projects) {
        this.projects = projects.stream().map(SimpleProjectDto::new).collect(Collectors.toList());
    }
}
