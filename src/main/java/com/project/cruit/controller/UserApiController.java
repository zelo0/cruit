package com.project.cruit.controller;

import com.project.cruit.authentication.CurrentUser;
import com.project.cruit.authentication.SessionUser;
import com.project.cruit.domain.*;
import com.project.cruit.domain.stack.Stack;
import com.project.cruit.dto.PageWrapper;
import com.project.cruit.dto.ResponseWrapper;
import com.project.cruit.exception.InvalidPageOffsetException;
import com.project.cruit.exception.NotHaveSessionException;
import com.project.cruit.service.StackService;
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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserApiController {
    private final UserService userService;
    private final StackService stackService;

    @GetMapping("")
    public PageWrapper<SearchUserResponse> searchUsers(@RequestParam(name = "q", defaultValue = "") String stackFilter,
                                                                          @RequestParam(name="page", defaultValue = "0") int page,
                                                                          @RequestParam(name = "limit", defaultValue = "12") int limit,
                                                       @RequestParam(name="leader", defaultValue = "all") String leaderFilter) {
        Page<User> users;
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id"));
        if (stackFilter.isBlank()) {
            // 굳이 조인할 필요 없으니 if문으로 분기
            users =userService.findByNoStackFilter(pageRequest, leaderFilter);
        } else {
            List<String> stackFilterList = List.of(stackFilter.split(";"));
            users = userService.findByStackFilter(stackFilterList, leaderFilter, pageRequest);
        }
        // page offset이 너무 크면 에러
        if (users.getTotalPages() != 0 && users.getTotalPages() <= page) {
            throw new InvalidPageOffsetException();
        }

        List<SearchUserResponse> responses = users.stream().map(SearchUserResponse::new).collect(Collectors.toList());
        return new PageWrapper(responses, users.hasPrevious(), users.hasNext(), users.getTotalPages(), users.getNumber());
    }

    @GetMapping("/{userId}")
    public ResponseWrapper getUser(@PathVariable Long userId) {
        User targetUser = userService.findById(userId);
        return new ResponseWrapper(new GetUserResponse(targetUser));
    }

    @GetMapping("/me")
    public ResponseWrapper<GetMeResponse> getMe(@CurrentUser SessionUser sessionUser) {
        if (sessionUser == null) {
            throw new NotHaveSessionException();
        }

        User me = userService.findById(sessionUser.getId());
        return new ResponseWrapper<>(new GetMeResponse(me, stackService.findAllByPosition(me.getPosition())));
    }

    @GetMapping("/me/name")
    public ResponseWrapper<GetMyNicknameResponse> getMyName(@CurrentUser SessionUser sessionUser) {
        // session이 없으면 빈 문자열 반환
        if (sessionUser == null) {
            return new ResponseWrapper<>(new GetMyNicknameResponse(""));
        }

        // sessionUser와 실제 데이터베이스에 있는 데이터가  sync 안 맞는 문제
        
        return new ResponseWrapper<>(new GetMyNicknameResponse(sessionUser.getNickname()));
    }

    @PostMapping("")
    public ResponseWrapper<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        User user = new User(request.getEmail(), request.getPassword(), request.getName(), request.getPosition());
        return new ResponseWrapper<>(new CreateUserResponse(userService.join(user)));
    }

    @PatchMapping("/me/name")
    public ResponseWrapper<SetMyNameResponse> setMyName(@RequestBody @Valid SetMyNameRequest request, @CurrentUser SessionUser sessionUser) {
        if (sessionUser == null) {
            throw new NotHaveSessionException();
        }
        String changedName = userService.setName(sessionUser.getId(), request.getName());
        return new ResponseWrapper<>(new SetMyNameResponse(changedName));
    }

    @PatchMapping("/me/position")
    public ResponseWrapper<SetMyPositionResponse> setMyPosition(@RequestBody @Valid SetMyPositionRequest request, @CurrentUser SessionUser sessionUser) {
        if (sessionUser == null) {
            throw new NotHaveSessionException();
        }
        Position changedPosition = userService.setPosition(sessionUser.getId(), request.getPosition());
        List<? extends Stack> selectableStacks = stackService.findAllByPosition(changedPosition);
        return new ResponseWrapper<>(new SetMyPositionResponse(changedPosition.name(), (List<Stack>) selectableStacks));
    }

    @PatchMapping("/me/canBeLeader")
    public ResponseWrapper<SetMyCanBeLeaderResponse> setMyCanBeLeader(@RequestBody @Valid SetMyCanBeLeaderRequest request, @CurrentUser SessionUser sessionUser) {
        if (sessionUser == null) {
            throw new NotHaveSessionException();
        }
        System.out.println("request.getCanBeLeader() = " + request.getCanBeLeader());
        Boolean changedCanBeLeader = userService.setCanBeLeader(sessionUser.getId(), request.getCanBeLeader());
        return new ResponseWrapper<>(new SetMyCanBeLeaderResponse(changedCanBeLeader));
    }

    @PatchMapping("/me/stacks")
    public ResponseWrapper<SetMyStacksResponse> setMyStacks(@RequestBody @Valid SetMyStacksRequest request, @CurrentUser SessionUser sessionUser) {
        if (sessionUser == null) {
            throw new NotHaveSessionException();
        }
        List<Stack> changedStacks = userService.setUserStacks(sessionUser.getId(), request.getStacks());
        return new ResponseWrapper<>(new SetMyStacksResponse(changedStacks));
    }

    @PatchMapping("/me/introduction")
    public ResponseWrapper<SetMyIntroductionResponse> setMyIntroduction(@RequestBody @Valid SetMyIntroductionRequest request, @CurrentUser SessionUser sessionUser) {
        if (sessionUser == null) {
            throw new NotHaveSessionException();
        }
        String changedIntroduction = userService.setIntroduction(sessionUser.getId(), request.getIntroduction());
        return new ResponseWrapper<>(new SetMyIntroductionResponse(changedIntroduction));
    }

    @PatchMapping("/me/github")
    public ResponseWrapper<SetMyGithubResponse> setMyGithub(@RequestBody @Valid SetMyGithubRequest request, @CurrentUser SessionUser sessionUser) {
        if (sessionUser == null) {
            throw new NotHaveSessionException();
        }
        String changedGithub = userService.setGithub(sessionUser.getId(), request.getGithub());
        return new ResponseWrapper<>(new SetMyGithubResponse(changedGithub));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class CreateUserRequest {
        @NotEmpty
        @Email(message = "유효한 이메일이 아닙니다")
        private String email;

        @NotEmpty
        @Size(min = 8, max = 30, message = "비밀번호는 8자 이상 30자 이하여야 합니다")
        private String password;

        @NotEmpty
        @Size(max = 30, message = "이름은 1글자 이상 30자 이하여야 합니다")
        private String name;

        @NotEmpty
        @Pattern(regexp = "frontend|backend|design") // 정해진 값이 맞는 지 확인
        private String position;

        public User toUser() {
            return new User(this.getEmail(), this.getPassword(), this.getName(), this.getPosition());
        }
    }

    @Data
    @AllArgsConstructor
    static class CreateUserResponse {
        @NotEmpty
        private Long userId;
    }

    @Data
    static class GetMeResponse {
        private Long id;
        private String email;
        private String name;
        private Position position;
        private List<Stack> availableStacks;
        private List<? extends Stack> selectableStacks;
        private String introduction;
        private String profile;
        private String github;
        private List<String> links;
//        private Double rating;
        private Boolean canBeLeader;

        public GetMeResponse(User me, List<? extends Stack> selectableStacks) {
            id = me.getId();
            email = me.getEmail();
            name = me.getName();
            position = me.getPosition();
            availableStacks = me.getUserStacks().stream().map(userStack -> userStack.getStack()).collect(Collectors.toList());
            this.selectableStacks = selectableStacks;
            introduction = me.getIntroduction();
            profile = me.getProfile();
            github = me.getGithub();
            links = me.getLinks();
//            rating = me.getRating();
            canBeLeader = me.getCanBeLeader();
        }
    }

    @Data
    @AllArgsConstructor
    static class GetMyNicknameResponse {
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class SetMyNameRequest {
        @NotEmpty
        private String name;
    }

    @Data
    static class SetMyNameResponse {
        private String name;
        public SetMyNameResponse(String changedName) {
            this.name = changedName;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class SetMyPositionRequest {
        private String position;
    }

    @Data
    @AllArgsConstructor
    static class SetMyPositionResponse {
        private String position;
        private List<Stack> selectableStacks;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class SetMyCanBeLeaderRequest {
        private Boolean canBeLeader;
    }

    @Data
    @AllArgsConstructor
    static class SetMyCanBeLeaderResponse {
        private Boolean canBeLeader;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class SetMyStacksRequest {
        private List<Stack> stacks;
    }

    @Data
    @AllArgsConstructor
    static class SetMyStacksResponse {
        private List<Stack> availableStacks;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class SetMyIntroductionRequest {
        private String introduction;
    }

    @Data
    @AllArgsConstructor
    static class SetMyIntroductionResponse {
        private String introduction;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class SetMyGithubRequest {
        private String github;
    }

    @Data
    @AllArgsConstructor
    static class SetMyGithubResponse {
        private String github;
    }

    @Data
    static class SearchUserResponse {
        private Long id;
        private String name;
        private String profile;
        private String github;
        private String position;
        private List<String> stacks = new ArrayList<>();
        private Boolean canBeLeader;

        public SearchUserResponse(User user) {
            this.id = user.getId();
            this.name = user.getName();
            this.profile = user.getProfile();
            this.github = user.getGithub();
            this.position = user.getPosition().name();
            this.canBeLeader = user.getCanBeLeader();

            List<UserStack> userStacks = user.getUserStacks();
            for (UserStack userStack : userStacks) {
                stacks.add(userStack.getStack().getImage());
            }
        }
    }

    @Data
    static class StackImage {
        private String image;

        public StackImage(Stack stack) {
            image = stack.getImage();
        }
    }

    @Data
    static class GetUserResponse {
        private Long id;
        private String name;
        private String profile;
        private String github;
        private Boolean canBeLeader;
        private String position;
        private String introduction;
        private List<String> linkList;
        private List<Stack> stackList = new ArrayList<>();
        private List<SimpleProjectInfo> projectList = new ArrayList<>();

        public GetUserResponse(User user) {
            id = user.getId();
            name = user.getName();
            profile = user.getProfile();
            github = user.getGithub();
            canBeLeader = user.getCanBeLeader();
            position = user.getPosition().name();
            introduction = user.getIntroduction();
            linkList = user.getLinks();

            List<UserStack> userStacks = user.getUserStacks();
            for (UserStack userStack : userStacks) {
                stackList.add(userStack.getStack());
            }

            List<UserPart> userParts = user.getUserParts();
            for (UserPart userPart : userParts) {
                projectList.add(new SimpleProjectInfo(userPart.getPart().getProject()));
            }
        }
    }

    @Data
    static class SimpleProjectInfo {
        private Long id;
        private String name;

        public SimpleProjectInfo(Project project) {
            this.id = project.getId();
            this.name = project.getName();
        }
    }
}
