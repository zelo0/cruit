package com.project.cruit.repository;

import com.project.cruit.domain.Position;
import com.project.cruit.domain.Project;
import com.project.cruit.domain.User;
import com.project.cruit.domain.UserPart;
import com.project.cruit.domain.part.FrontendPart;
import com.project.cruit.domain.part.Part;
import com.project.cruit.domain.stack.Stack;
import com.project.cruit.domain.status.ProjectStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class ProjectRepositoryTest {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    private void initPersistenceContext() {
        User user = testEntityManager.persist(new User("test", "test@g.com", "test", Position.FRONTEND.name()));

        Project project1 = testEntityManager.persist(new Project(user, "test", "test"));

        Project project2 = new Project(user, "test2", "test2");
        project2.setStatus(ProjectStatus.PRIVATE);
        testEntityManager.persist(project2);

        Stack stack = testEntityManager.persist(new Stack("react", "image"));

        FrontendPart frontendPart1 = new FrontendPart(project1);
        frontendPart1.addStack(stack);
        testEntityManager.persist(frontendPart1);
        project1.addPart(frontendPart1);

        FrontendPart frontendPart2 = new FrontendPart(project2);
        frontendPart2.addStack(stack);
        testEntityManager.persist(frontendPart2);
        project2.addPart(frontendPart2);
    }

    @Test
    @DisplayName("유저가 프로젝트에 참여 중인 파트 수")
    void isMemberInLong() {
        // given
        User user = user();
        testEntityManager.persist((user));
        Project project = new Project(user, "new", "new");
        testEntityManager.persist(project);
        FrontendPart frontendPart = new FrontendPart(project);
        testEntityManager.persist(frontendPart);
        UserPart userPart = new UserPart(user, frontendPart);
        testEntityManager.persist(userPart);

        // when
        Long result = projectRepository.isMemberInLong(project.getId(), user.getId());

        // then
        assertThat(result).isEqualTo(1);
    }

    private User user() {
        return new User("new", "new@g.com", "new", Position.FRONTEND.name());
    }

    @Test
    @DisplayName("오직 public 상태인 프로젝트만")
    void getOnlyPublicProjects() {
        // given

        // when
        PageRequest pageRequest = PageRequest.of(0, 12, Sort.by(Sort.Direction.DESC, "id"));
        Page<Project> page = projectRepository.findAllPublic(pageRequest);

        // then
        Assertions.assertThat(page.getTotalElements()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findByStackFilter 또한 public한 프로젝트만 get")
    void getOnlyPublicProjects_whenFindByStackFilter() {
        // given

        // when
        PageRequest pageRequest = PageRequest.of(0, 12, Sort.by(Sort.Direction.DESC, "id"));
        Page<Project> page = projectRepository.findByStackFilter(List.of("react"), pageRequest);

        // then
        Assertions.assertThat(page.getTotalElements()).isEqualTo(1L);
    }
}