package com.project.cruit.controller;

import com.project.cruit.entity.Position;
import com.project.cruit.entity.User;
import com.project.cruit.entity.UserStack;
import com.project.cruit.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;

    @PostMapping("/api/users")
    public CreateUserResponse createUser(@RequestBody @Valid CreateUserRequest request) {
        try {
            User user = new User();
            // 초기화 필요
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setName(request.getName());
            user.setPosition(request.getPosition());
            user.setUserStacks(request.getUserStacks());
            user.setIntroduction(request.getIntroduction());
            user.setProfile(request.getProfile());
            user.setGithub(request.getGithub());
            user.setCanBeLeader(request.getCanBeLeader());

            return new CreateUserResponse(userService.join(user), "success");
        } catch (IllegalStateException e) {
            return new CreateUserResponse(null, e.getMessage());
        }
    }

    @Data
    @AllArgsConstructor
    static class CreateUserRequest {
        @NotEmpty
        private String email;
        @NotEmpty
        private String password;
        @NotEmpty
        private String name;
        @NotEmpty
        private Position position;
        private List<UserStack> userStacks;
        private String introduction;
        private String profile;
        private String github;
        @NotEmpty
        private Boolean canBeLeader;
    }

    @Data
    @AllArgsConstructor
    static class CreateUserResponse {
        private Long id;
        private String message;
    }
}
