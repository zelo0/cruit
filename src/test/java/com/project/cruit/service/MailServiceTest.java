package com.project.cruit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {
    @InjectMocks
    private MailService mailService;

    @Mock
    private JavaMailSenderImpl javaMailSender;


    @Test
    @DisplayName("이메일 보내기 성공")
    void sendEmail_success() {
        // given

        // when
        mailService.sendProposalMail("greenman7@gmail.com", "test 이메일");

        // then
        Mockito.verify(javaMailSender).send(any(SimpleMailMessage.class));
    }
}