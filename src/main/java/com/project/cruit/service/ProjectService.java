package com.project.cruit.service;

import com.project.cruit.domain.User;
import com.project.cruit.domain.UserPart;
import com.project.cruit.domain.part.BackendPart;
import com.project.cruit.domain.part.DesignPart;
import com.project.cruit.domain.part.FrontendPart;
import com.project.cruit.domain.part.Part;
import com.project.cruit.domain.Project;
import com.project.cruit.domain.status.ProjectStatus;
import com.project.cruit.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final UserPartService userPartService;

    public Page<Project> findAll(Pageable pageable) {
        return projectRepository.findAllPublic(pageable);
    }

    @Transactional
    public Long saveProject(Project project) {
        Project savedProject = projectRepository.save(project);

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

        return savedProject.getId();
    }

    public Page<Project> findByStackFilter(List<String> stacks, Pageable pageable) {
        return projectRepository.findByStackFilter(stacks, pageable);
    }

    public Project findById(Long projectId) {
        return projectRepository.findById(projectId).get();
    }

    @Transactional
    public Long modifyText(Long projectId, String name, String description) {
        Project project = projectRepository.findById(projectId).get();
        project.setName(name);
        project.setDescription(description);

        return project.getId();
    }

    @Transactional
    public void delete(Project project) {
        projectRepository.delete(project);
    }

    @Transactional
    public void modifyStatus(Long id, String status) {
        Project project = projectRepository.findById(id).get();
        if (status == "PUBLIC") {
            project.setStatus(ProjectStatus.PUBLIC);
        } else {
            project.setStatus(ProjectStatus.PRIVATE);
        }
    }

    public List<Project> findAllProjectByUserId(Long userId) {
        User user = userService.findById(userId);
        List<UserPart> userParts = userPartService.findAllByUser(user);
        return userParts.stream().map(userPart -> userPart.getPart().getProject()).collect(Collectors.toList());
    }
}
