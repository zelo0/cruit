package com.project.cruit.repository;

import com.project.cruit.domain.Position;
import com.project.cruit.domain.Project;
import com.project.cruit.domain.User;
import com.project.cruit.domain.part.BackendPart;
import com.project.cruit.domain.part.FrontendPart;
import com.project.cruit.domain.part.Part;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProposalRepositoryTest {
    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("프로젝트 제안할 권한이 없는 경우 - 0L 리턴")
    void notHaveAuthorityToProposeProject() {
        // given
        User proposer = new User("test@test.com", "testPassword", "test", Position.FRONTEND.name());
        testEntityManager.persist(proposer);
        Project project = new Project(proposer, "testProject", "test");
        testEntityManager.persist(project);
        Part frontendPart = new FrontendPart(project);
        frontendPart.addMember(proposer);
        testEntityManager.persist(frontendPart);

        User notHavingAuthorityUser = new User("test2@test.com", "testPassword", "testBack", Position.BACKEND.name());
        testEntityManager.persist(notHavingAuthorityUser);
        Part backendPart = new BackendPart(project);
        backendPart.addMember(notHavingAuthorityUser);
        testEntityManager.persist(backendPart);

        // when
        // 백엔드 파트 멤버가 프론트엔드 파트로 초대 - 0L 리턴
        Long result = proposalRepository.isAvailableToProposeToUser(frontendPart.getId(), notHavingAuthorityUser.getId());

        // then
        assertThat(result).isEqualTo(0L);
    }
}