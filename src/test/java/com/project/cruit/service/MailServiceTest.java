package com.project.cruit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {MailService.class, MailSenderAutoConfiguration.class})
class MailServiceTest {
    @Autowired
    private MailService mailService;


    @Test
    @DisplayName("이메일 보내기 성공")
    void sendEmail_success() {
        // given

        // when
        mailService.sendProposalMail("greenman7@gmail.com", "test 이메일");

        // then
    }
}