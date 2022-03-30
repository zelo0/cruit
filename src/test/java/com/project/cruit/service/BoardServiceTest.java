package com.project.cruit.service;

import com.project.cruit.domain.Board;
import com.project.cruit.domain.Position;
import com.project.cruit.domain.Project;
import com.project.cruit.domain.User;
import com.project.cruit.dto.CreateBoardRequest;
import com.project.cruit.exception.NotHaveSessionException;
import com.project.cruit.repository.BoardRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {
    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProjectService projectService;

    @Test
    @DisplayName("boardRepository의 save 호츨")
    void save() {
        // given
        User user = user();
        doReturn(user).when(userService).findById(anyLong());
        Project project = project(user);
        doReturn(project).when(projectService).findById(anyLong());
        CreateBoardRequest request = new CreateBoardRequest(1L, "test", "test");

        // when
        boardService.save(request, 1L);

        // then
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @Test
    @DisplayName("이 게시물의 저자가 맞는지 체크 - 성공")
    void checkIsAuthor_success() {
        // given
        User user = user();
        Project project = project(user);
        Board board = board(user, project);
        doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());

        // when

        // then
        assertDoesNotThrow(() -> boardService.checkIsAuthor(board.getId(), user.getId()));
    }

    @Test
    @DisplayName("이 게시물의 저자가 맞는지 체크 - 실패")
    void checkIsAuthor_fail() {
        // given
        User user = user();
        Project project = project(user);
        Board board = board(user, project);
        Long anotherUserId = 2L;
        doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());

        // when

        // then
        assertThrows(NotHaveSessionException.class, () -> boardService.checkIsAuthor(board.getId(), anotherUserId));
    }


    User user() {
        return User.builder()
                .id(1L)
                .name("test")
                .email("test")
                .password("test")
                .position(Position.FRONTEND)
                .build();
    }

    Project project(User user) {
        return Project.builder()
                .id(1L)
                .proposer(user)
                .name("test")
                .description("test")
                .build();
    }

    Board board(User user, Project project) {
        return Board.builder()
                .id(1L)
                .title("test")
                .content("test")
                .project(project)
                .writer(user)
                .build();
    }
}