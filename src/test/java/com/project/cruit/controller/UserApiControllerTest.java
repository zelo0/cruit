package com.project.cruit.controller;

import com.google.gson.Gson;
import com.project.cruit.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(SpringExtension.class)
//@WebMvcTest(UserApiController.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserApiControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void create_invalidEmail_shouldFailAndReturn400() throws Exception {
        UserApiController.CreateUserRequest payload = new UserApiController.CreateUserRequest();
        payload.setEmail("");
        payload.setPassword("1234");
        payload.setName("na");
        payload.setPosition("frontend");

        Gson gson = new Gson();
        mvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(payload)))
                .andExpect(status().is(400));
    }
}