package com.project.cruit.controller;

import com.project.cruit.authentication.CurrentUser;
import com.project.cruit.authentication.SessionUser;
import com.project.cruit.domain.Question;
import com.project.cruit.domain.User;
import com.project.cruit.dto.QuestionDto;
import com.project.cruit.dto.ResponseWrapper;
import com.project.cruit.dto.SimpleMessageBody;
import com.project.cruit.exception.NotHaveSessionException;
import com.project.cruit.exception.NotPermitException;
import com.project.cruit.service.ProjectService;
import com.project.cruit.service.QuestionService;
import com.project.cruit.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/questions")
public class QuestionApiController {
    private final QuestionService questionService;
    private final UserService userService;
    private final ProjectService projectService;

    // 질문 등록
    @PostMapping("")
    public ResponseWrapper createQuestion(@CurrentUser SessionUser sessionUser, @RequestBody @Valid CreateQuestionRequest request) {


        Question question = new Question(userService.findById(sessionUser.getId()), request.getContent(),
                projectService.findById(request.getProjectId()), null);
        questionService.save(question);
        return new ResponseWrapper(new QuestionDto(question));
    }
    
    // 질문의 질문 등록
    @PostMapping("/sub")
    public ResponseWrapper createSubQuestion(@CurrentUser SessionUser sessionUser, @RequestBody @Valid CreateSubQuestionRequest request) {


        Question parentQuestion = questionService.findById(request.getParentId());
        Question question = new Question(userService.findById(sessionUser.getId()), request.getContent(),
                projectService.findById(request.getProjectId()), parentQuestion);

        questionService.addChild(parentQuestion, question);
        return new ResponseWrapper(new QuestionDto(question));
    }

    // 질문 수정
    @PatchMapping("/{questionId}")
    public ResponseWrapper modifyQuestion(@CurrentUser SessionUser sessionUser, @PathVariable Long questionId, @RequestBody @Valid ModifyQuestionRequest request) {



        // 본인의 질문인지 확인
        Question question = questionService.findById(questionId);
        if (!sessionUser.getId().equals(question.getQuestioner().getId())) {
            throw new NotPermitException("본인의 댓글만 수정 가능합니다");
        }

        questionService.modifyContent(question, request.getContent());
        return new ResponseWrapper(new SimpleMessageBody("성공"));
    }

    // 질문 삭제
    @DeleteMapping("/{questionId}")
    public ResponseWrapper deleteQuestion(@CurrentUser SessionUser sessionUser, @PathVariable Long questionId) {


        // 본인의 질문인지 확인
        Question question = questionService.findById(questionId);
        if (!sessionUser.getId().equals(question.getQuestioner().getId())) {
            throw new NotPermitException("본인의 댓글만 삭제 가능합니다");
        }

        questionService.delete(question);
        return new ResponseWrapper(new SimpleMessageBody("성공"));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class CreateQuestionRequest {
        @NotNull
        private Long projectId;
        @NotBlank
        private String content;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class ModifyQuestionRequest {
        @NotBlank
        private String content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class CreateSubQuestionRequest {
        @NotNull
        private Long projectId;
        @NotNull
        private Long parentId;
        @NotBlank
        private String content;
    }
}
