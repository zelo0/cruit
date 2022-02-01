package com.project.cruit.service;

import com.project.cruit.entity.part.BackendPart;
import com.project.cruit.entity.part.DesignPart;
import com.project.cruit.entity.part.FrontendPart;
import com.project.cruit.entity.part.Part;
import com.project.cruit.entity.Project;
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

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Transactional
    public void saveProject(Project project) {
        projectRepository.save(project);

        Part frontendPart = new FrontendPart(project);
        Part backendPart = new BackendPart(project);
        Part designPart = new DesignPart(project);

        switch (project.getProposer().getPosition()) {
            case FRONTEND:
                frontendPart.addMember(project.getProposer());
                break;
            case BACKEND:
                backendPart.addMember(project.getProposer());
                break;
            case DESIGN:
                designPart.addMember(project.getProposer());
                break;
        }

        project.addPart(frontendPart);
        project.addPart(backendPart);
        project.addPart(designPart);
    }

    public List<Project> findByStackFilter(List<String> stacks) {
        return projectRepository.findByStackFilter(stacks);
    }
}
