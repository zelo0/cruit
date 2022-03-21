package com.project.cruit.service;

import com.project.cruit.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {
    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    private TestEntityManager testEntityManager;

//    @Mock
//    private UserService userService;
//
//    @Mock
//    private ProjectService projectService;

    @Test
    @DisplayName("게시판 삭제")
    void deleteBoard() {
        // given

        // when

        // then
    }
}