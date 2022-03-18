package com.project.cruit.dto;

import com.project.cruit.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UserNameImageDto {
    private Long id;
    private String name;
    private String profile;

    public UserNameImageDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.profile = user.getProfile();
    }
}
