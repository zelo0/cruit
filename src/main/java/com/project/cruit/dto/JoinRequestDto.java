package com.project.cruit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequestDto {
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

//        public User toUser() {
//            return new User(this.getEmail(), this.getPassword(), this.getName(), this.getPosition());
//        }
}
