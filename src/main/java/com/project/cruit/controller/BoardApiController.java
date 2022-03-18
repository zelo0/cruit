package com.project.cruit.controller;

import com.project.cruit.authentication.CurrentUser;
import com.project.cruit.authentication.SessionUser;
import com.project.cruit.dto.CreateBoardRequest;
import com.project.cruit.dto.ResponseWrapper;
import com.project.cruit.dto.SimpleMessageBody;
import com.project.cruit.service.BoardService;
import com.project.cruit.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
public class BoardApiController {
    private final ProjectService projectService;
    private final BoardService boardService;

    @PostMapping("")
    public ResponseWrapper<SimpleMessageBody> createBoard(@RequestBody @Valid CreateBoardRequest request, @CurrentUser SessionUser sessionUser) {
        sessionUser.checkIsNull();
        projectService.checkIsMember(request.getProjectId(), sessionUser.getId());
        boardService.save(request, sessionUser.getId());
        return new ResponseWrapper<>(new SimpleMessageBody("게시물 작성 성공"));
    }


}
