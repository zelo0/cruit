package com.project.cruit.service;

import com.project.cruit.domain.Position;
import com.project.cruit.domain.Project;
import com.project.cruit.domain.User;
import com.project.cruit.domain.UserPart;
import com.project.cruit.domain.part.FrontendPart;
import com.project.cruit.domain.part.Part;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartServiceTest {
    @InjectMocks
    PartService partService;

    @Mock
    NotificationService notificationService;

    @Mock
    UserService userService;

    @Mock
    UserPartService userPartService;


    @Test
    @DisplayName("멤버 삭제 시 그 멤버에게 알림 전달 - createNonReferenceNotification 호출")
    void makeNotification_whenDeleteMember() {
        // given
        User user = User.builder()
                .id(1L)
                .email("test")
                .password("test")
                .name("test")
                .position(Position.FRONTEND)
                .build();

        Project project = new Project(user, "test", "test");
        Part part = new FrontendPart(project);
        UserPart userPart = new UserPart(user, part);

        doReturn(user).when(userService).findById(anyLong());
        doReturn(userPart).when(userPartService).findByPartAndUser(part, user);
        
        // when
        partService.deleteMember(part, user.getId());

        // then
        verify(notificationService, times(1)).createNonReferenceNotification(any(User.class), anyString());
    }
}