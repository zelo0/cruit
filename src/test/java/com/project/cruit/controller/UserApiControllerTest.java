package com.project.cruit.controller;

import com.google.gson.Gson;
import com.project.cruit.exception.EmailExistsException;
import com.project.cruit.exception.NameExistsException;
import com.project.cruit.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserApiController.class)
class UserApiControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    private Gson gson = new Gson();

    @Test
    public void create_invalidEmail_shouldFailAndReturn400() throws Exception {
        UserApiController.CreateUserRequest payload = new UserApiController.CreateUserRequest();
        payload.setEmail("");
        payload.setPassword("12345678");
        payload.setName("na");
        payload.setPosition("FRONTEND");

        mvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(payload)))
                .andExpect(status().is(400));
    }

    @Test
    public void create_existedName_shouldFailAndReturn400() throws  Exception {
        UserApiController.CreateUserRequest payload = new UserApiController.CreateUserRequest();
        payload.setEmail("already@gmail.com");
        payload.setPassword("12345678");
        payload.setName("na");
        payload.setPosition("FRONTEND");

        doThrow(new NameExistsException()).when(userService).join(payload.toUser());

        mvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(payload)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message").value("이미 존재하는 이름입니다"));
    }

    @Test
    public void create_existedEmail_shouldFailAndReturn400() throws  Exception {
        UserApiController.CreateUserRequest payload = new UserApiController.CreateUserRequest();
        payload.setEmail("already@gmail.com");
        payload.setPassword("12345678");
        payload.setName("na");
        payload.setPosition("FRONTEND");

        doThrow(new EmailExistsException()).when(userService).join(payload.toUser());

        mvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(payload)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message").value("이미 존재하는 이메일입니다"));
    }
    
//    사용자 기술 스택 변경할 때 기존 데이터 테이블에서 제거되는지 확인하는 테스트
}