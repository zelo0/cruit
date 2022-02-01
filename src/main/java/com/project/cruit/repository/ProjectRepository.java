package com.project.cruit.repository;

import com.project.cruit.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("select p from Project p join p.parts pa join pa.partStacks ps join ps.stack s where s.name in :stacks")
    List<Project> findByStackFilter(@Param("stacks") List<String> stacks);
}
