package com.project.cruit.service;

import com.project.cruit.entity.Part;
import com.project.cruit.entity.Position;
import com.project.cruit.entity.Project;
import com.project.cruit.entity.UserPart;
import com.project.cruit.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final PartService partService;
    private final UserPartService userPartService;

    @Transactional
    public void saveProject(Project project) {
        projectRepository.save(project);

        Part frontendPart = new Part(project, Position.FRONTEND);
        Part backendPart = new Part(project, Position.BACKEND);
        Part designPart = new Part(project, Position.DESIGN);
        partService.saveParts(List.of(frontendPart, backendPart, designPart));

        UserPart userPart = null;
        switch (project.getProposer().getPosition()) {
            case FRONTEND:
                userPart = new UserPart(project.getProposer(), frontendPart, false);
                frontendPart.getUserParts().add(userPart);
            case BACKEND:
                userPart = new UserPart(project.getProposer(), backendPart, false);
                backendPart.getUserParts().add(userPart);
            case DESIGN:
                userPart = new UserPart(project.getProposer(), designPart, false);
                designPart.getUserParts().add(userPart);
        }
        userPartService.saveUserPart(userPart);

    }
}
