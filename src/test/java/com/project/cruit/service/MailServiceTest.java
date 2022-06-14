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

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.any;

// mimeMessage를 사용하면 session이 필요한데 mock할 수 없음
//@ExtendWith(MockitoExtension.class)
//class MailServiceTest {
//    @InjectMocks
//    private MailService mailService;
//
//    @Mock
//    private JavaMailSenderImpl javaMailSender;
//
//
//    @Test
//    @DisplayName("이메일 보내기 성공")
//    void sendEmail_success() throws MessagingException {
//        // given
//
//        // when
//        mailService.sendProposalMail("test@gmail.com", "test 이메일");
//
//        // then
//        Mockito.verify(javaMailSender).send(any(MimeMessage.class));
//    }
//}