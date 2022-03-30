package com.project.cruit.service;

import com.project.cruit.domain.Board;
import com.project.cruit.domain.Project;
import com.project.cruit.domain.User;
import com.project.cruit.dto.CreateBoardRequest;
import com.project.cruit.dto.ModifyBoardRequest;
import com.project.cruit.exception.NotHaveSessionException;
import com.project.cruit.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;

    private final ProjectService projectService;
    private final UserService userService;

    @Transactional
    public Board save(CreateBoardRequest request, Long userId) {
        Project project = projectService.findById(request.getProjectId());
        User user = userService.findById(userId);
        Board board = new Board(request.getTitle(), request.getContent(), project, user);
        return boardRepository.save(board);
    }


    public void checkIsAuthor(Long boardId, Long userId) {
        Board board = boardRepository.findById(boardId).get();
        Long writerId = board.getWriter().getId();
        if (!userId.equals(writerId)) {
            throw new NotHaveSessionException();
        }
    }

    @Transactional
    public Board modifyBoard(Long boardId, ModifyBoardRequest request) {
        Board board = boardRepository.findById(boardId).get();
        board.setContent(request.getContent());
        board.setTitle(request.getTitle());
        return board;
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).get();
        boardRepository.delete(board);
    }

    public Board findById(Long boardId) {
        return boardRepository.findById(boardId).get();
    }
}
