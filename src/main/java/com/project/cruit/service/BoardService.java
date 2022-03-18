package com.project.cruit.service;

import com.project.cruit.domain.Board;
import com.project.cruit.domain.Project;
import com.project.cruit.domain.User;
import com.project.cruit.dto.CreateBoardRequest;
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


    public void save(CreateBoardRequest request, Long userId) {
        Project project = projectService.findById(request.getProjectId());
        User user = userService.findById(userId);
        Board board = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .project(project)
                .writer(user)
                .build();
        boardRepository.save(board);
    }


}
