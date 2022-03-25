package com.project.cruit.controller;

import com.project.cruit.authentication.CurrentUser;
import com.project.cruit.authentication.SessionUser;
import com.project.cruit.domain.*;
import com.project.cruit.domain.notification.Notification;
import com.project.cruit.domain.notification.ProposalNotification;
import com.project.cruit.domain.notification.QuestionNotification;
import com.project.cruit.domain.stack.Stack;
import com.project.cruit.dto.*;
import com.project.cruit.service.NotificationService;
import com.project.cruit.service.S3UploaderService;
import com.project.cruit.service.StackService;
import com.project.cruit.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserApiController {
    private final UserService userService;
    private final StackService stackService;
    private final NotificationService notificationService;
    private final S3UploaderService s3UploaderService;

    @GetMapping("")
    public PageWrapper<List<SearchUserDto>> searchUsers(@RequestParam(name = "q", defaultValue = "") String stackFilter,
                                                  @RequestParam(name="page", defaultValue = "0") int page,
                                                  @RequestParam(name = "limit", defaultValue = "12") int limit,
                                                  @RequestParam(name="leader", defaultValue = "all") String leaderFilter) {

        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id"));
        Page<User> users = userService.findPageByStackAndLeader(pageRequest, stackFilter, leaderFilter, page);

        List<SearchUserDto> responses = users.stream().map(SearchUserDto::new).collect(Collectors.toList());
        return new PageWrapper<>(responses, users.hasPrevious(), users.hasNext(), users.getTotalPages(), users.getNumber());
    }

    @GetMapping("/{userId}")
    public ResponseWrapper getUser(@PathVariable Long userId) {
        User targetUser = userService.findById(userId);
        List<Project> publicProjectsInvolved = userService.findPublicProjectsInvolved(targetUser);
        return new ResponseWrapper(new DetailUserDto(targetUser, publicProjectsInvolved));
    }

    @GetMapping("/me")
    public ResponseWrapper<GetMeResponse> getMe(@CurrentUser SessionUser sessionUser) {
        SessionUser.checkIsNull(sessionUser);

        User me = userService.findById(sessionUser.getId());
        return new ResponseWrapper<>(new GetMeResponse(me, stackService.findAllByPosition(me.getPosition().name())));
    }

    // head에서만은 로그인 안 돼있어도 예외 발생시키지 않기
    // 안 그러면 메인 페이지에서 계속 로그인 페이지로 이동함
    @GetMapping("/me/head")
    public ResponseWrapper<GetMyHeadResponse> getMyHead(@CurrentUser SessionUser sessionUser) {
        log.info("header에서 데이터 요청");
        // session이 없으면 빈 문자열 반환
        if (sessionUser == null) {
            return new ResponseWrapper<>(new GetMyHeadResponse("", "", 0, new ArrayList<>()));
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
            } else {
                // relatedId가 없는, 메시지만 있는 notification이면
                unReadNotificationDtos.add(new NotificationDto(notification, null));
            }
        }
        return new ResponseWrapper<>(new GetMyHeadResponse(user.getName(), user.getPosition().name(), unReadCount, unReadNotificationDtos));
    }


    @PostMapping("")
    public ResponseWrapper<CreateUserResponse> createUser(@RequestBody @Valid JoinRequestDto request) {
        User user = userService.join(request);
        return new ResponseWrapper<>(new CreateUserResponse(user.getId()));
    }

    @PatchMapping("/me/profile")
    public ResponseWrapper setMyProfile(@RequestPart("file") MultipartFile file, @CurrentUser SessionUser sessionUser) throws IOException {
        SessionUser.checkIsNull(sessionUser);


        // s3에 업로드
        String fileUrl = s3UploaderService.upload(file, "profiles");

        userService.setProfile(sessionUser.getId(), fileUrl);
        return new ResponseWrapper(new SetMyProfileResponse(fileUrl));
    }

    @PatchMapping("/me/name")
    public ResponseWrapper<SetMyNameResponse> setMyName(@RequestBody @Valid SetMyNameRequest request, @CurrentUser SessionUser sessionUser) {
        SessionUser.checkIsNull(sessionUser);

        String changedName = userService.setName(sessionUser.getId(), request.getName());
        return new ResponseWrapper<>(new SetMyNameResponse(changedName));
    }

    @PatchMapping("/me/position")
    public ResponseWrapper<SetMyPositionResponse> setMyPosition(@RequestBody @Valid SetMyPositionRequest request, @CurrentUser SessionUser sessionUser) {
        SessionUser.checkIsNull(sessionUser);

        Position changedPosition = userService.setPosition(sessionUser.getId(), request.getPosition());
        List<? extends Stack> selectableStacks = stackService.findAllByPosition(changedPosition.name());
        return new ResponseWrapper<>(new SetMyPositionResponse(changedPosition.name(), (List<Stack>) selectableStacks));
    }

    @PatchMapping("/me/canBeLeader")
    public ResponseWrapper<SetMyCanBeLeaderResponse> setMyCanBeLeader(@RequestBody @Valid SetMyCanBeLeaderRequest request, @CurrentUser SessionUser sessionUser) {
        SessionUser.checkIsNull(sessionUser);

        System.out.println("request.getCanBeLeader() = " + request.getCanBeLeader());
        Boolean changedCanBeLeader = userService.setCanBeLeader(sessionUser.getId(), request.getCanBeLeader());
        return new ResponseWrapper<>(new SetMyCanBeLeaderResponse(changedCanBeLeader));
    }

    @PatchMapping("/me/stacks")
    public ResponseWrapper<SetMyStacksResponse> setMyStacks(@RequestBody @Valid SetMyStacksRequest request, @CurrentUser SessionUser sessionUser) {
        SessionUser.checkIsNull(sessionUser);

        List<Stack> changedStacks = userService.setUserStacks(sessionUser.getId(), request.getStacks());
        return new ResponseWrapper<>(new SetMyStacksResponse(changedStacks));
    }

    @PatchMapping("/me/introduction")
    public ResponseWrapper<SetMyIntroductionResponse> setMyIntroduction(@RequestBody @Valid SetMyIntroductionRequest request, @CurrentUser SessionUser sessionUser) {
        SessionUser.checkIsNull(sessionUser);

        String changedIntroduction = userService.setIntroduction(sessionUser.getId(), request.getIntroduction());
        return new ResponseWrapper<>(new SetMyIntroductionResponse(changedIntroduction));
    }

    @PatchMapping("/me/github")
    public ResponseWrapper<SetMyGithubResponse> setMyGithub(@RequestBody @Valid SetMyGithubRequest request, @CurrentUser SessionUser sessionUser) {
        SessionUser.checkIsNull(sessionUser);

        String changedGithub = userService.setGithub(sessionUser.getId(), request.getGithub());
        return new ResponseWrapper<>(new SetMyGithubResponse(changedGithub));
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
        private String position;
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
    static class StackImage {
        private String image;

        public StackImage(Stack stack) {
            image = stack.getImage();
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
