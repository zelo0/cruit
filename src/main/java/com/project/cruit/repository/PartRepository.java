package com.project.cruit.repository;

import com.project.cruit.domain.Project;
import com.project.cruit.domain.part.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {
    Part findByProjectAndPosition(Project project, String position);

    @Query("select p from Part p join fetch p.userParts up join fetch up.user where p.id = :partId")
    Part findByIdWithUsers(@Param("partId") Long partId);

    @Query("select p from Part p join fetch p.partStacks ps join fetch ps.stack join fetch p.project pr " +
            "where p.id = :partId")
    Part findByIdWithProjectAndStacks(@Param("partId") Long partId);

    @Query("select p from Part p join fetch p.project pr where p.position = :position and pr.proposer.id = :userId")
    List<Part> findPartsByPositionAndProjectProposer(@Param("position") String position, @Param("userId") Long userId);

    // 유저가 어떤 파트의 리더라는 건 그 파트의 포지션이 유저의 포지션과 일치한다는 의미
    @Query("select p from Part p join fetch p.project pr join p.userParts up where up.isLeader = true and up.user.id = :userId")
    List<Part> findPartsByPartLeader(@Param("userId") Long userId);
}
