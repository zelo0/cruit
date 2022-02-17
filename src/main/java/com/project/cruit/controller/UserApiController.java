package com.project.cruit.controller;

import com.project.cruit.authentication.CurrentUser;
import com.project.cruit.authentication.SessionUser;
import com.project.cruit.domain.*;
import com.project.cruit.dto.ResponseWrapper;
import com.project.cruit.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;

    @GetMapping("/api/v1/users/me")
    public ResponseWrapper getMe(@CurrentUser SessionUser sessionUser) {
        User me = userService.findById(sessionUser.getId());
        return new ResponseWrapper(new GetMeResponse(me));
    }

    @GetMapping("/api/v1/users/me/nickname")
    public ResponseWrapper getMyNickname(@CurrentUser SessionUser sessionUser) {
        return new ResponseWrapper(new GetMyNicknameResponse(sessionUser.getNickname()));
    }

    @PostMapping("/api/v1/users")
    public ResponseWrapper createUser(@RequestBody @Valid CreateUserRequest request) {
        User user = new User(request.getEmail(), request.getPassword(), request.getName(), request.getPosition());
        return new ResponseWrapper(new CreateUserResponse(userService.join(user)));
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
        private Long userId;
    }

    @Data
    static class GetMeResponse {
        private Long id;
        private String email;
        private String name;
        private Position position;
        private List<UserStack> userStacks;
        private String introduction;
        private String profile;
        private String github;
        private List<String> links;
        private Double rating;
        private Boolean canBeLeader;

        public GetMeResponse(User me) {
            id = me.getId();
            email = me.getEmail();
            name = me.getName();
            position = me.getPosition();
            userStacks = me.getUserStacks();
            introduction = me.getIntroduction();
            profile = me.getProfile();
            github = me.getGithub();
            links = me.getLinks();
            rating = me.getRating();
            canBeLeader = me.getCanBeLeader();
        }
    }

    @Data
    static class GetMyNicknameResponse {
        private String nickname;

        public GetMyNicknameResponse(String nickname) {
            this.nickname = nickname;
        }
    }
}
