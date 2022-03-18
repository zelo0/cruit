package com.project.cruit.repository;

import com.project.cruit.domain.Position;
import com.project.cruit.domain.Project;
import com.project.cruit.domain.User;
import com.project.cruit.domain.part.FrontendPart;
import com.project.cruit.domain.part.Part;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ProjectRepositoryTest {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    private void initPersistenceContext() {
        User user = testEntityManager.persist(user());
        Project project = testEntityManager.persist(new Project(user, "test", "test"));
        FrontendPart frontendPart = testEntityManager.persist(new FrontendPart(project));
        frontendPart.addMember(user);
    }

    @Test
    @DisplayName("유저가 프로젝트에 참여 중인 파트 수")
    void isMemberInLong() {
        // given

        // when
        Long result = projectRepository.isMemberInLong(1L, 1L);

        // then
        assertThat(result).isEqualTo(1);
    }

    private User user() {
        return User.builder()
                .name("test")
                .email("test@g.com")
                .password("test")
                .position(Position.FRONTEND)
                .build();
    }
}