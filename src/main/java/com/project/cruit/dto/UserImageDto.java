package com.project.cruit.dto;

import com.project.cruit.domain.User;
import lombok.Data;

@Data
public class UserImageDto {
    private String profile;

    public UserImageDto(User user) {
        profile = user.getProfile();
    }
}
