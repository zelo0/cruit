package com.project.cruit.dto;

import com.project.cruit.domain.Project;
import lombok.Data;

@Data
public class SimpleProjectInfoDto {
    private Long id;
    private String name;

    public SimpleProjectInfoDto(Project project) {
        this.id = project.getId();
        this.name = project.getName();
    }
}

