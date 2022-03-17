package com.project.cruit.dto;

import com.project.cruit.domain.Project;
import com.project.cruit.domain.part.Part;
import com.project.cruit.domain.status.PartStatus;
import lombok.Data;

import java.util.List;

@Data
public class SimpleProjectDto {
    private Long id;
    private String proposerProfile;
    private String proposerName;
    private PartStatus status;
    //        private String description;
    private String name;
    private SimplePartDto frontendPart;
    private SimplePartDto backendPart;
    private SimplePartDto designPart;

    public SimpleProjectDto(Project project) {
        id = project.getId();
        proposerProfile = project.getProposer().getProfile();
        proposerName = project.getProposer().getName();
//            description = project.getDescription();
        name = project.getName();
        List<Part> parts = project.getParts();
        for (Part part : parts) {
            switch (part.getPosition()) {
                case "FRONTEND":
                    frontendPart = new SimplePartDto(part);
                    break;
                case "BACKEND":
                    backendPart = new SimplePartDto(part);
                    break;
                case "DESIGN":
                    designPart = new SimplePartDto(part);
                    break;
            }
        }
    }
}