package com.codingmate.email.service;

import com.codingmate.config.properties.EmailProperties;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.email.EmailMessagingException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


/**
 * 이메일을 보내기 위한 서비스 클래스
 *
 * @author duskafka
 */
@Slf4j
@Service
public class EmailSendService {
    private final JavaMailSender mailSender;
    private final String subject;
    private final String url;
    private final String from;
    private final String template;
    private final SpringTemplateEngine templateEngine;

    public EmailSendService(
            JavaMailSender mailSender,
            EmailProperties emailProperties,
            SpringTemplateEngine templateEngine
    ) {
        this.mailSender = mailSender;
        this.subject = emailProperties.subject();
        this.url = emailProperties.url();
        this.from = emailProperties.username();
        this.template = emailProperties.template();
        this.templateEngine = templateEngine;
    }

    /**
     * 이메일을 보내기 위한 메소드
     *
     * @param to                인증이 필요한 이메일
     * @param verificationToken 인증을 위해 발급한 토큰
     * @throws EmailMessagingException 이메일을 구성하거나 전송하는 과정에서 발생하는 문제를 BusinessException으로 감싼다.
     */
    public void sendHtmlEmail(String to, String verificationToken) {
        try {
            log.debug("[EmailSendService] sendHtmlEmail({}, {})", to, verificationToken);
            // 새로운 이메일 메시지 객체 생성
            MimeMessage message = mailSender.createMimeMessage();

            // 메시지를 편리하게 조작하기 위한 헬퍼 객체 생성 (true: multipart, "UTF-8": 문자 인코딩)
            MimeMessageHelper helper = new MimeMessageHelper(
                    message, true, "UTF-8"
            );


            helper.setTo(to);           // 받는 사람 이메일 주소 설정
            helper.setSubject(subject); // 이메일 제목(subject) 설정
            helper.setFrom(from);       // 보내는 사람 이메일 주소 설정

            // 이메일 본문 내용(HTML 형식) 생성 — 버튼 포함된 인증 링크 HTML
            String htmlContent = generateHtmlContent(verificationToken);

            helper.setText(htmlContent, true);  // true = HTML 모드

            mailSender.send(message);   // 실제 이메일 발송
            log.info("[EmailSendService] 이메일이 제대로 발송되었습니다: email={}", to);
        } catch (MessagingException e) {
            // 예외 발생 시 커스텀 예외로 감싸 사용자에게 전달한다
            throw new EmailMessagingException(
                    ErrorMessage.EMAIL_MESSAGING,
                    "이메일 생성 중 에러가 발생했습니다."
            );
        }
    }

    /**
     * 인증 이메일 바디를 만들기 위한 메소드
     */
    private String generateHtmlContent(String verificationToken) {
        Context context = new Context();
        String verificationUrl = generateUrl(verificationToken);
        context.setVariable("verificationUrl", verificationUrl);
        return templateEngine.process("email_verification", context);
    }

    /**
     * 인증 버튼에 들어갈 URL을 만들어주는 메소드
     */
    private String generateUrl(String verificationToken) {
        return url + "?token=" + URLEncoder.encode(verificationToken, StandardCharsets.UTF_8);
    }
}