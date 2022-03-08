package com.project.cruit.dto;

import com.project.cruit.domain.User;
import lombok.Data;

@Data
public class UserAvatar {
    private Long id;
    private String name;
    private String profile;


    public UserAvatar(User user) {
        id = user.getId();
        name = user.getName();
        profile = user.getProfile();
    }
}
