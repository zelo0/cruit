package com.project.cruit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MailService {
    @Autowired
    private JavaMailSenderImpl mailSender;

    @Value("${spring.mail.username}")
    private String serviceEmail;


    /*
    * 메시지만 전달받는 형태
    * 메일 제목이랑 내용은 메시지 바탕으로 생성
    * */
    @Async
    public void sendProposalMail(String to, String message) {
        log.info("sent proposal email");
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(serviceEmail);
        mail.setTo(to);
        mail.setSubject("[cruitapp] " + message);
        mail.setText(message + " https://app.cruitapp.com/proposals/me 에 접속해서 지금 확인해보세요.");
        mailSender.send(mail);
    }

}
