package com.project.cruit.authentication;

import com.google.gson.Gson;
import com.project.cruit.dto.SimpleMessageBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
    @Autowired
    Gson gson;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/json");

        log.error("authentication error occured - type: {}, message: {}", exception.getClass(), exception.getMessage());
        SimpleMessageBody body;
        if (exception instanceof InsufficientAuthenticationException) {
            body = new SimpleMessageBody("필드 값이 비정상입니다");
        } else if (exception instanceof BadCredentialsException) {
            body = new SimpleMessageBody("로그인 정보가 일치하지 않습니다");
        } else {
            body = new SimpleMessageBody("로그인에 실패했습니다");
        }

        response.getWriter().write(gson.toJson(body));
    }
}
