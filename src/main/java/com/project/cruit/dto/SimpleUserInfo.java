package com.project.cruit.dto;

import com.project.cruit.domain.User;
import lombok.Data;

@Data
public class SimpleUserInfo {
    private Long id;
    private String name;
    private String profile;
    private Boolean isLeader;

    public SimpleUserInfo(User user, Boolean isLeader) {
        id = user.getId();
        name = user.getName();
        profile = user.getProfile();
        this.isLeader = isLeader;
    }
}