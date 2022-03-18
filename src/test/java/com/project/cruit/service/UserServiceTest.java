package com.project.cruit.service;

import com.project.cruit.domain.Position;
import com.project.cruit.domain.User;
import com.project.cruit.dto.JoinRequestDto;
import com.project.cruit.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 성공 - 비밀번호 암호화")
    void join() {
        // given
        JoinRequestDto joinRequest = joinRequest();

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPw = encoder.encode(joinRequest.getPassword());
        doReturn(User.builder()
                .email(joinRequest.getEmail())
                .password(encryptedPw)
                .name(joinRequest.getName())
                .position(Position.FRONTEND)
                .build()).when(userRepository).save(any(User.class));
        
        // when
        User joinedUser = userService.join(joinRequest);

        // then
        assertThat(joinedUser.getEmail()).isEqualTo(joinRequest.getEmail());
        assertThat(encoder.matches(joinRequest.getPassword(), joinedUser.getPassword())).isTrue();
        assertThat(joinedUser.getName()).isEqualTo(joinRequest.getName());
        assertThat(joinedUser.getPosition()).isEqualTo(Position.FRONTEND);
    }

    private JoinRequestDto joinRequest() {
        return new JoinRequestDto("test@test.com", "test", "test", "FRONTEND");
    }
}