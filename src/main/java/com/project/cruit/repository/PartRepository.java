package com.project.cruit.repository;

import com.project.cruit.entity.Project;
import com.project.cruit.entity.part.Part;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, Long> {
    Part findByProjectAndPosition(Project project, String backend);
}
