package com.project.cruit.controller;

import com.project.cruit.advice.TrackExecutionTime;
import com.project.cruit.authentication.CurrentUser;
import com.project.cruit.authentication.SessionUser;
import com.project.cruit.domain.Board;
import com.project.cruit.dto.*;
import com.project.cruit.service.BoardService;
import com.project.cruit.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
public class BoardApiController {
    private final ProjectService projectService;
    private final BoardService boardService;

    @TrackExecutionTime
    @PostMapping("")
    public ResponseWrapper<SimpleMessageBody> createBoard(@RequestBody @Valid CreateBoardRequest request, @CurrentUser SessionUser sessionUser) {
        SessionUser.checkIsNull(sessionUser);
        projectService.checkIsMember(request.getProjectId(), sessionUser.getId());
        boardService.save(request, sessionUser.getId());
        return new ResponseWrapper<>(new SimpleMessageBody("게시물 작성 성공"));
    }

    @GetMapping("/{boardId}")
    public ResponseWrapper<BoardContentDto> getBoardContent(@PathVariable Long boardId) {
        Board board = boardService.findById(boardId);
        return new ResponseWrapper<>(new BoardContentDto(board));
    }

    @PatchMapping("/{boardId}")
    public ResponseWrapper<SimpleMessageBody> modifyBoard(@RequestBody @Valid ModifyBoardRequest request, @PathVariable Long boardId, @CurrentUser SessionUser sessionUser) {
        SessionUser.checkIsNull(sessionUser);

        boardService.checkIsAuthor(boardId, sessionUser.getId());
        boardService.modifyBoard(boardId, request);
        return new ResponseWrapper<>(new SimpleMessageBody("게시물 수정 성공"));
    }

    @DeleteMapping("/{boardId}")
    public ResponseWrapper<SimpleMessageBody> deleteBoard(@PathVariable Long boardId, @CurrentUser SessionUser sessionUser) {
        SessionUser.checkIsNull(sessionUser);

        boardService.checkIsAuthor(boardId, sessionUser.getId());
        boardService.deleteBoard(boardId);
        return new ResponseWrapper<>(new SimpleMessageBody("게시물 삭제 성공"));
    }
}
