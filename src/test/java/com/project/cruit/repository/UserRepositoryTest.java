package com.project.cruit.repository;

import com.project.cruit.domain.Position;
import com.project.cruit.domain.Project;
import com.project.cruit.domain.User;
import com.project.cruit.domain.UserPart;
import com.project.cruit.domain.part.FrontendPart;
import com.project.cruit.domain.part.Part;
import com.project.cruit.domain.status.ProjectStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("내가 속한 public 프로젝트 정상적으로 가져오기")
    void getOnlyPublicProjectsInvolved_whenPublicProjectExists() {
        // given
        // public한 프로젝트 하나 넣어놓기
        User user = new User("test", "test@g.com", "test", Position.FRONTEND.name());
        entityManager.persist(user);
        Project project = new Project(user, "test", "test");
        entityManager.persist(project);
        Part part = new FrontendPart(project);
        entityManager.persist(part);
        UserPart userPart = new UserPart(user, part);
        entityManager.persist(userPart);
        project.setStatus(ProjectStatus.PUBLIC);

        // when
        List<Project> involvedProjects = userRepository.findAllPublicProjectsInvolved(user);

        // then
        Assertions.assertThat(involvedProjects).hasSize(1);
    }

    @Test
    @DisplayName("내가 속한 private한 프로젝트는 조회하지 않는다")
    void getOnlyPublicProjectsInvolved_whenOnlyPrivateProjectExists() {
        // given
        // private한 프로젝트 하나 넣어놓기
        User user = new User("test", "test@g.com", "test", Position.FRONTEND.name());
        entityManager.persist(user);
        Project project = new Project(user, "test", "test");
        entityManager.persist(project);
        Part part = new FrontendPart(project);
        entityManager.persist(part);
        UserPart userPart = new UserPart(user, part);
        entityManager.persist(userPart);
        project.setStatus(ProjectStatus.PRIVATE);

        // when
        List<Project> involvedProjects = userRepository.findAllPublicProjectsInvolved(user);

        // then
        Assertions.assertThat(involvedProjects).hasSize(0);
    }
}