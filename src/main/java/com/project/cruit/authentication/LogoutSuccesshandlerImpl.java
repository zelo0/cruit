package com.project.cruit.authentication;

import com.google.gson.Gson;
import com.project.cruit.dto.SimpleMessageBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutSuccesshandlerImpl implements LogoutSuccessHandler {
    @Autowired
    Gson gson;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/json");

        SimpleMessageBody body = new SimpleMessageBody("로그아웃에 성공했습니다");
        response.getWriter().write(gson.toJson(body));
    }
}
