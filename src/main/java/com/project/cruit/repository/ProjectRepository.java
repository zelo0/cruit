package com.project.cruit.repository;

import com.project.cruit.domain.Project;
import com.project.cruit.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("select p from Project p join p.parts pa join pa.partStacks ps join ps.stack s where s.name in :stacks and p.status = 'PUBLIC'")
    Page<Project> findByStackFilter(@Param("stacks") List<String> stacks, Pageable pageable);

    @Query("select p from Project p where p.status = 'PUBLIC'")
    Page<Project> findAllPublic(Pageable pageable);

    @Query("select p from Project p join p.parts pa join pa.userParts up where up.user.id = :userId and p.id = :projectId")
    Project getProjectIfMember(@Param("projectId") Long projectId, @Param("userId") Long userId);

//    Page<Project> findAllEfficient(Pageable pageable);
}
