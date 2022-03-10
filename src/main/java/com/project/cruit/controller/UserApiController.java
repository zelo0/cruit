package com.project.cruit.controller;

import com.project.cruit.authentication.CurrentUser;
import com.project.cruit.authentication.SessionUser;
import com.project.cruit.domain.*;
import com.project.cruit.domain.notification.Notification;
import com.project.cruit.domain.notification.ProposalNotification;
import com.project.cruit.domain.notification.QuestionNotification;
import com.project.cruit.domain.stack.Stack;
import com.project.cruit.dto.PageWrapper;
import com.project.cruit.dto.ResponseWrapper;
import com.project.cruit.dto.SimpleMessageBody;
import com.project.cruit.exception.InvalidPageOffsetException;
import com.project.cruit.exception.NotHaveSessionException;
import com.project.cruit.service.NotificationService;
import com.project.cruit.service.S3UploaderService;
import com.project.cruit.service.StackService;
import com.project.cruit.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserApiController {
    private final UserService userService;
    private final StackService stackService;
    private final NotificationService notificationService;
    private final S3UploaderService s3UploaderService;

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

    @GetMapping("/me/head")
    public ResponseWrapper<GetMyHeadResponse> getMyHead(@CurrentUser SessionUser sessionUser) {
        // session이 없으면 빈 문자열 반환
        if (sessionUser == null) {
            return new ResponseWrapper<>(new GetMyHeadResponse("", 0, new ArrayList<>()));
        }

        // sessionUser와 실제 데이터베이스에 있는 데이터가  sync 안 맞는 문제 -  쿼리 필요
        User user = userService.findById(sessionUser.getId());
        long unReadCount = notificationService.getUnReadNotificationCount(user);
        List<Notification> unReadNotifications = notificationService.getUnReadNotifications(user);

        List<NotificationDto> unReadNotificationDtos = new ArrayList<>();
        for (Notification notification : unReadNotifications) {
            if (notification.getType().equals("question")) {
                // QuestionNotification이면 projectId 찾아서 생성
                QuestionNotification questionNotification = (QuestionNotification) notification;
                unReadNotificationDtos.add(new NotificationDto(notification, questionNotification.getQuestion().getProject().getId()));
            } else if (notification.getType().equals("proposal")) {
                // ProposalNotification이면 relatedId 값으로 proposalId 넘겨줌
                ProposalNotification proposalNotification = (ProposalNotification) notification;
                unReadNotificationDtos.add(new NotificationDto(notification, proposalNotification.getProposal().getId()));
            }
        }
        return new ResponseWrapper<>(new GetMyHeadResponse(user.getName(), unReadCount, unReadNotificationDtos));
    }


    @PostMapping("")
    public ResponseWrapper<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        User user = new User(request.getEmail(), request.getPassword(), request.getName(), request.getPosition());
        return new ResponseWrapper<>(new CreateUserResponse(userService.join(user)));
    }

    @PatchMapping("/me/profile")
    public ResponseWrapper setMyProfile(@RequestPart("file") MultipartFile file, @CurrentUser SessionUser sessionUser) throws IOException {
        if (sessionUser == null) {
            throw new NotHaveSessionException();
        }

        // s3에 업로드
        String fileUrl = s3UploaderService.upload(file, "profiles");

        userService.setProfile(sessionUser.getId(), fileUrl);
        return new ResponseWrapper(new SetMyProfileResponse(fileUrl));
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
        @Pattern(regexp = "FRONTEND|BACKEND|DESIGN") // 정해진 값이 맞는 지 확인
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
    static class GetMyHeadResponse {
        private String name;
        private long notificationCount;
        private List<NotificationDto> notifications;
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

    @Data
    @AllArgsConstructor
    static class SetMyProfileResponse {
        private String profile;
    }

    @Data
    @AllArgsConstructor
    static class NotificationDto {
        private Long id;
        private Long relatedId; // question이면 projectId, proposal이면 proposalId
        private String message;
        private String type;

        public NotificationDto(Notification notification, Long relatedId) {
            this.id = notification.getId();
            this.message = notification.getMessage();
            this.type = notification.getType();
            this.relatedId = relatedId;
        }
    }
}
