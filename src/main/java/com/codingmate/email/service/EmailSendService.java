package com.codingmate.email.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSendService {
    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String token) {
        String subject = "이메일 인증을 완료해주세요";
        String link = "https://yourdomain.com/verify-email?token=" + token;
        String text = "다음 링크를 클릭하여 이메일 인증을 완료하세요:\n" + link;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}