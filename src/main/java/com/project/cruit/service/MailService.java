package com.project.cruit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

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
    public void sendProposalMail(String to, String message) throws MessagingException {
        log.info("started to send proposal email from " + serviceEmail + " to " + to);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");

        messageHelper.setFrom(serviceEmail);
        messageHelper.setTo(to);
        messageHelper.setSubject("[cruitapp] 나와 프로젝트를 함께하고 싶어하는 사람이 있어요. 어서 확인해보세요");
        messageHelper.setText("<p>" + message + "</p>" +
                "<p><a href='https://app.cruitapp.com/proposals/me'>여기</a>에 접속해서 바로 확인해보세요.</p>", true);

        mailSender.send(mimeMessage);
    }

}
