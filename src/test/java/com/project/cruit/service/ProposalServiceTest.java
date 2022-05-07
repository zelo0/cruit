package com.project.cruit.service;

import com.project.cruit.exception.NotPermitException;
import com.project.cruit.repository.ProposalRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class ProposalServiceTest {
    @InjectMocks
    private ProposalService proposalService;

    @Mock
    private ProposalRepository proposalRepository;

    @Test
    @DisplayName("프로젝트를 유저에게 제한할 권한 있나 체크(실패) - 오류 메시지")
    void checkAuthorityToApprove_fail() {
        // given
        doReturn(0L).when(proposalRepository).isAvailableToProposeToUser(anyLong(), anyLong());

        // when
        // then
        assertThrows(NotPermitException.class, () -> {
            proposalService.checkAuthorityToPropose(0L, 0L);});
    }
}