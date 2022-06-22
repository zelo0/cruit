package com.project.cruit.controller;

import com.project.cruit.authentication.CurrentUser;
import com.project.cruit.authentication.SessionUser;
import com.project.cruit.dto.*;
import com.project.cruit.domain.*;
import com.project.cruit.exception.InvalidPageOffsetException;
import com.project.cruit.exception.NotHaveSessionException;
import com.project.cruit.exception.NotPermitException;
import com.project.cruit.service.ProjectService;
import com.project.cruit.service.QuestionService;
import com.project.cruit.service.UserPartService;
import com.project.cruit.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
public class ProjectApiController {
    private final ProjectService projectService;
    private final UserService userService;
    private final QuestionService questionService;
    private final UserPartService userPartService;

    @GetMapping("")
    public PageWrapper<SimpleProjectDto> getProjects(@RequestParam(name = "q", defaultValue = "") String stackFilter,
                                                     @RequestParam(name="page", defaultValue = "0") int page,
                                                     @RequestParam(name = "limit", defaultValue = "12") int limit) {
        Page<Project> projects;
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id"));
        if (stackFilter.isBlank()) {
            projects =projectService.findAll(pageRequest);
        } else {
            List<String> stackFilterList = List.of(stackFilter.split(";"));
            projects = projectService.findByStackFilter(stackFilterList, pageRequest);
        }
        // page offset이 너무 크면 에러
        if (projects.getTotalPages() != 0 && projects.getTotalPages() <= page) {
            throw new InvalidPageOffsetException();
        }

        List<SimpleProjectDto> responses = projects.stream().map(SimpleProjectDto::new).collect(Collectors.toList());
        return new PageWrapper(responses, projects.hasPrevious(), projects.hasNext(), projects.getTotalPages(), projects.getNumber());
    }

    /* 프로젝트 제안자가 프로젝트 기본 정보 수정하려 할 때 접근 */
    @GetMapping("/simple/{projectId}")
    public ResponseWrapper getProjectSimple(@CurrentUser SessionUser sessionUser, @PathVariable Long projectId) {
        SessionUser.checkIsNull(sessionUser);



        // 프로젝트 제안자가 아닌데 접근하려 하면 exception
        Project targetProject = projectService.findById(projectId);
        if (!targetProject.getProposer().getId().equals(sessionUser.getId())) {
            throw new NotPermitException("프로젝트 제안자만 가능합니다");
        }

        return new ResponseWrapper(new GetProjectSimpleResponse(targetProject));
    }

    // 내가 참여 중인 프로젝트 보여주기
    @GetMapping("/me")
    public ResponseWrapper getMyProjects(@CurrentUser SessionUser sessionUser) {
        SessionUser.checkIsNull(sessionUser);



        List<Project> myProjects = projectService.findAllProjectByUserId(sessionUser.getId());

        return new ResponseWrapper(new GetMyProjectsResponse(myProjects));
    }

    @PatchMapping("/text")
    public ResponseWrapper setProjectText(@CurrentUser SessionUser sessionUser, @RequestBody SetProjectTextRequest request) {



        // 프로젝트 제안자가 아닌데 수정하려 하면 exception
        Project targetProject = projectService.findById(request.getId());

        if (!targetProject.getProposer().getId().equals(sessionUser.getId())) {
            throw new NotPermitException("프로젝트 제안자만 수정 가능합니다");
        }

        Long afterChangeProjectId = projectService.modifyText(request.getId(), request.getName(), request.getDescription());
        return new ResponseWrapper(new SetProjectTextResponse(afterChangeProjectId));
    }

    @PatchMapping("/status")
    public ResponseWrapper setProjectStatus(@CurrentUser SessionUser sessionUser, @RequestBody SetProjectStatusRequest request) {



        // 프로젝트 제안자가 아닌데 수정하려 하면 exception
        Project targetProject = projectService.findById(request.getId());

        if (!targetProject.getProposer().getId().equals(sessionUser.getId())) {
            throw new NotPermitException("프로젝트 제안자만 수정 가능합니다");
        }

        projectService.modifyStatus(request.getId(), request.getStatus());
        return new ResponseWrapper(new SimpleMessageBody("상태 변경 성공"));
    }

    @PostMapping("")
    public ResponseWrapper createProject(@CurrentUser SessionUser sessionUser, @RequestBody @Valid CreateProjectRequest request) {



        User proposer = userService.findById(sessionUser.getId());
        Project project = new Project(proposer, request.getName(), request.getDescription());
        Long projectId = projectService.saveProject(project);
        return new ResponseWrapper(new createProjectResponse(projectId));
    }

    @GetMapping("/{projectId}")
    public ResponseWrapper getProject(@PathVariable Long projectId) {
        Project project = projectService.findById(projectId);
        // 부모 없는 질문만 가져와야지 프로젝트의 질문을 다 가져오면 질문의 질문에도 있어서 중복 발생
        List<Question> hierarchicalQuestions = questionService.findQuestionsByProjectIdAndParentNonExists(project);
        return new ResponseWrapper(new DetailProjectDto(project, hierarchicalQuestions));
    }
    
    @DeleteMapping("/{projectId}")
    public ResponseWrapper deleteProject(@PathVariable Long projectId, @CurrentUser SessionUser sessionUser) {


        // 프로젝트 제안자가 아닌데 삭제하려 하면 exception
        Project targetProject = projectService.findById(projectId);

        if (!targetProject.getProposer().getId().equals(sessionUser.getId())) {
            throw new NotPermitException("프로젝트 제안자만 삭제 가능합니다");
        }

        projectService.delete(targetProject);
        
        return new ResponseWrapper(new SimpleMessageBody("삭제 성공"));
    }

    @GetMapping("/{projectId}/boards")
    public ResponseWrapper<GetBoardsResponseDto> getBoardsOfProject(@PathVariable Long projectId, @CurrentUser SessionUser sessionUser) {
        SessionUser.checkIsNull(sessionUser);

        projectService.checkIsMember(projectId, sessionUser.getId());
        return new ResponseWrapper<>(new GetBoardsResponseDto(projectService.getBoards(projectId).stream().map(DetailBoardDto::new).collect(Collectors.toList())));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class CreateProjectRequest {
        @NotEmpty
        private String name;
        @NotEmpty
        private String description;
    }



    @Data
    @AllArgsConstructor
    static class createProjectResponse {
        @NotEmpty
        Long projectId;
    }


    @Data
    @AllArgsConstructor
    static class GetProjectSimpleResponse {
        private String name;
        private String description;
        private String status;


        public GetProjectSimpleResponse(Project project) {
            this.name = project.getName();
            this.status = project.getStatus().name();
            this.description = project.getDescription();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class SetProjectTextRequest {
        private Long id;
        private String name;
        private String description;
    }

    @Data
    @AllArgsConstructor
    static class SetProjectTextResponse {
        private Long projectId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class SetProjectStatusRequest {
        private Long id;
        private String status;
    }
}
