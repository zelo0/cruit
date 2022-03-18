package com.project.cruit.service;

import com.project.cruit.exception.NotPermitException;
import com.project.cruit.repository.ProjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Test
    @DisplayName("프로젝트 멤버가 아니면 fail")
    void checkIsMember_fail() {
        // given
        Long projectId = 10L;
        Long userId = 1L;
        doReturn(0L).when(projectRepository).isMemberInLong(projectId, userId);

        // when
        RuntimeException exception = assertThrows(NotPermitException.class, () -> {
            projectService.checkIsMember(projectId, userId);
        });

        // then
    }

}