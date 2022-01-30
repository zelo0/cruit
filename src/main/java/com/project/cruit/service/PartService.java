package com.project.cruit.service;

import com.project.cruit.entity.Project;
import com.project.cruit.entity.User;
import com.project.cruit.entity.part.Part;
import com.project.cruit.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartService {
    private final PartRepository partRepository;

    @Transactional
    public void saveParts(List<Part> partList) {
        partRepository.saveAll(partList);
    }


    public Part getBackendPart(Project project) {
        return partRepository.findByProjectAndPosition(project, "backend");
    }
    public Part getFrontendPart(Project project) {
        return partRepository.findByProjectAndPosition(project, "frontend");
    }
    public Part getDesignPart(Project project) {
        return partRepository.findByProjectAndPosition(project, "design");
    }
}
