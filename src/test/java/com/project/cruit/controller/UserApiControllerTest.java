package com.project.cruit.controller;

import com.google.gson.Gson;
import com.project.cruit.authentication.AuthenticationFilter;
import com.project.cruit.domain.Position;
import com.project.cruit.domain.User;
import com.project.cruit.dto.PageWrapper;
import com.project.cruit.dto.SearchUserDto;
import com.project.cruit.exception.EmailExistsException;
import com.project.cruit.exception.InvalidPageOffsetException;
import com.project.cruit.exception.NameExistsException;
import com.project.cruit.repository.UserRepository;
import com.project.cruit.service.NotificationService;
import com.project.cruit.service.S3UploaderService;
import com.project.cruit.service.StackService;
import com.project.cruit.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.exceptions.stacktrace.StackTraceFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserApiController.class)
class UserApiControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;
    @MockBean
    private StackService stackService;
    @MockBean
    private NotificationService notificationService;
    @MockBean
    private S3UploaderService s3UploaderService;
    @MockBean
    private UserRepository userRepository;

    private Gson gson = new Gson();


    @Test
    @DisplayName("쿼리 없는 유저 검색")
    void searchUsersWithNoFilter() throws Exception {
        // given
        doReturn(userPage()).when(userService).findPageByStackAndLeader(pageRequest(), "", "all", 0);

        // when
        ResultActions resultActions = mvc.perform(get("/api/v1/users"));

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        PageWrapper<List<SearchUserDto>> response = gson.fromJson(mvcResult.getResponse().getContentAsString(), PageWrapper.class);
        assertThat(response.getData().size()).isEqualTo(5);
    }

    @Test
    @DisplayName("없는 이메일을 context로 넘길 시 error throw")
    void throwError_whenEmailDoesNtExist() throws Exception {
        // given
        String requestJson = "{ email: 'test@test.com', password: 'test'}";

        // when
        ResultActions result = mvc.perform(post("/api/v1/login").contentType(MediaType.APPLICATION_JSON).content(requestJson));

        // then
        result.andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("로그인 정보가 일치하지 않습니다"));
    }


    private class LoginRequestBody {
        private String email;
        private String password;

        public LoginRequestBody(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }


    // postman으로 테스트하면 이상 없음
//    @Test
//    @DisplayName("페이지 범위를 벗어나는 유저 검색")
//    void searchUsersWithInvalidPageOffset() throws Exception {
//        // given
//        PageRequest pageRequest = pageRequest();
//        doThrow((new InvalidPageOffsetException())).when(userService)
//                .findPageByStackAndLeader(pageRequest,
//                "", "all", 2);
//        doReturn(Page.empty()).when(userRepository).findAll(pageRequest());
//
//        // when
//        ResultActions resultActions = mvc.perform(get("/api/v1/users?page=2"));
//
//        // then
//        resultActions.andExpect(status().isBadRequest()).
//                andExpect(jsonPath("$.message").value("유효한 페이지 범위를 벗어났습니다."));
//    }


    private Page<User> userPage() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            users.add(new User("a@test.com", "test", "test", Position.DESIGN.name()));
//                    User.builder()
//                    .email("a@test.com")
//                    .password("test")
//                    .name("test")
//                    .position(Position.DESIGN)
//                    .build());
        }
        return new PageImpl<>(users);
    }

    // requestParam이 비었을 시 값들
    private PageRequest pageRequest() {
        return PageRequest.of(0, 12, Sort.by(Sort.Direction.DESC, "id"));
    }




//    @Test
//    public void create_invalidEmail_shouldFailAndReturn400() throws Exception {
//        CreateUserRequest payload = new CreateUserRequest();
//        payload.setEmail("");
//        payload.setPassword("12345678");
//        payload.setName("na");
//        payload.setPosition("FRONTEND");
//
//        mvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(payload)))
//                .andExpect(status().is(400));
//    }
//
//    @Test
//    public void create_existedName_shouldFailAndReturn400() throws  Exception {
//        CreateUserRequest payload = new CreateUserRequest();
//        payload.setEmail("already@gmail.com");
//        payload.setPassword("12345678");
//        payload.setName("na");
//        payload.setPosition("FRONTEND");
//
//        doThrow(new NameExistsException()).when(userService).join(payload.toUser());
//
//        mvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(payload)))
//                .andExpect(status().is(400))
//                .andExpect(jsonPath("$.message").value("이미 존재하는 이름입니다"));
//    }
//
//    @Test
//    public void create_existedEmail_shouldFailAndReturn400() throws  Exception {
//        CreateUserRequest payload = new CreateUserRequest();
//        payload.setEmail("already@gmail.com");
//        payload.setPassword("12345678");
//        payload.setName("na");
//        payload.setPosition("FRONTEND");
//
//        doThrow(new EmailExistsException()).when(userService).join(payload.toUser());
//
//        mvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(payload)))
//                .andExpect(status().is(400))
//                .andExpect(jsonPath("$.message").value("이미 존재하는 이메일입니다"));
//    }

//    사용자 기술 스택 변경할 때 기존 데이터 테이블에서 제거되는지 확인하는 테스트
}