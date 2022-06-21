package com.project.cruit.authentication;

import com.project.cruit.domain.User;
import com.project.cruit.exception.NotHaveSessionException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

@Slf4j
public class SessionUser implements UserDetails {
    private Long id;
    private String email;
    private String password;
    private String nickname;

    public SessionUser(User user) {
        id = user.getId();
        email = user.getEmail();
        password = user.getPassword();
        nickname = user.getName();
    }

    public String getNickname() {
        return nickname;
    }

    public Long getId() {
        return id;
    }

    public static void checkIsNull(SessionUser sessionUser) {
        if (sessionUser == null) {
            log.error("session이 없습니다");
            throw new NotHaveSessionException();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
