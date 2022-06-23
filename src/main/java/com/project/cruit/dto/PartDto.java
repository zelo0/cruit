package com.project.cruit.dto;

import com.project.cruit.domain.part.Part;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PartDto {
    private Long projectId;
    private String projectName;
    private String status;
    private Long id;
    private String position;
    private Boolean hasLeader;

    public PartDto(Part part, Boolean hasLeader) {
        this.projectId = part.getProject().getId();
        this.projectName = part.getProject().getName();
        this.status = part.getStatus().name();
        this.id = part.getId();
        this.position = part.getPosition();
        this.hasLeader = hasLeader;
    }
}