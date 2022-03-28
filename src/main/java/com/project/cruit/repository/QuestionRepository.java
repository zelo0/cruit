package com.project.cruit.repository;

import com.project.cruit.domain.Project;
import com.project.cruit.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("select q from Question q where q.project = :project and q.parent = null")
    List<Question> findQuestionsByProjectIdAndParentNonExists(@Param("project")Project project);
}
