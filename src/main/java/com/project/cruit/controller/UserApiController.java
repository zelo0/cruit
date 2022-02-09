package com.project.cruit.controller;

import com.project.cruit.dto.ResponseWrapper;
import com.project.cruit.entity.Position;
import com.project.cruit.entity.User;
import com.project.cruit.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;

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
        @Pattern(regexp = "(?=fronend)(?=backend)(?=design)") // 정해진 값이 맞는 지 확인
        private String position;
    }

    @Data
    @AllArgsConstructor
    static class CreateUserResponse {
        private Long userId;
    }
}
