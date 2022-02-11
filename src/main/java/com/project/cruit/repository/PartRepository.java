package com.project.cruit.repository;

import com.project.cruit.entity.Project;
import com.project.cruit.entity.part.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {
    Part findByProjectAndPosition(Project project, String backend);
}
