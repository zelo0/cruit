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
        this.id = user.getId();
        this.name = user.getName();
        this.profile = user.getProfile();
        this.isLeader = isLeader;
    }
}