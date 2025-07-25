package com.codingmate.service;

import com.codingmate.email.dto.EmailVerificationTokenResponse;
import com.codingmate.email.service.*;
import com.codingmate.exception.exception.email.IllegalEmailRegexException;
import com.codingmate.util.EmailValidateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailSendService emailSendService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private EmailVerificationValidator emailVerificationValidator;

    @Autowired
    private EmailVerificationTokenGenerator emailVerificationTokenGenerator;

    private final String testEmail = "sample@sample.com"; // 테스트할 이메일로 입력해주세요.


    /**
     * 컨트롤러의 흐름대로 실행되는 테스트. 이메일을 보내고 인증 확인까지 하는 비즈니스 로직 흐름을 테스트한다.
     *
     * @see EmailValidateUtil
     */
    @Test
    void emailVerification_Success() {
        assertThatCode(() -> {
            // 인증 요청이 들어와서 이메일로 요청을 보냄
            EmailValidateUtil.isValid(testEmail);
            String token = emailVerificationTokenGenerator.generateEmailVerificationToken(testEmail);
            emailSendService.sendHtmlEmail(testEmail, token);

            // 이메일에서 인증 요청을 수락했다고 가정하고 토큰으로 인증 요청을 처리함.
            EmailVerificationTokenResponse byToken = emailService.findByToken(token);
            boolean validEmailToken = emailVerificationValidator.isValidEmailToken(byToken);

            if (validEmailToken) emailVerificationService.verify(token);
        }).doesNotThrowAnyException();
    }

    /**
     * 이메일 형식 검사에 통과하는 테스트.
     * */
    @Test
    void emailValidateUtil_isValid_Success() {
        assertThatCode(() -> EmailValidateUtil.isValid(testEmail)).doesNotThrowAnyException();
        assertThatCode(() -> EmailValidateUtil.isValid("hong@hong.com")).doesNotThrowAnyException();
        assertThatCode(() -> EmailValidateUtil.isValid("korea@korea.com")).doesNotThrowAnyException();
        assertThatCode(() -> EmailValidateUtil.isValid("google@google.com")).doesNotThrowAnyException();
        assertThatCode(() -> EmailValidateUtil.isValid("naver@naver.com")).doesNotThrowAnyException();
    }

    /**
     * 이메일 형식 검사 통과에 실패하는 테스트.
     * */
    @Test
    void emailValidateUtil_isValid_Fail_WhenEmailInValid() {
        assertThatCode(() -> EmailValidateUtil.isValid("올바르지 않은 이메일 형식")).isInstanceOf(IllegalEmailRegexException.class);
        assertThatCode(() -> EmailValidateUtil.isValid("naver@naver")).isInstanceOf(IllegalEmailRegexException.class);
        assertThatCode(() -> EmailValidateUtil.isValid("naver@.com")).isInstanceOf(IllegalEmailRegexException.class);
        assertThatCode(() -> EmailValidateUtil.isValid("naver.com")).isInstanceOf(IllegalEmailRegexException.class);
        assertThatCode(() -> EmailValidateUtil.isValid("")).isInstanceOf(IllegalEmailRegexException.class);
    }

    /**
     * 이메일이 정상적으로 보내지는지 테스트하는 테스트 코드. 실제 입력한 email을 보면 정상적으로 보내지는 것을 확인할 수 있습니다.
     * */
    @Test
    void emailSendService_Success() {
        assertThatCode(() -> {
            EmailValidateUtil.isValid(testEmail);
            String token = emailVerificationTokenGenerator.generateEmailVerificationToken(testEmail);
            emailSendService.sendHtmlEmail(testEmail, token);
        }).doesNotThrowAnyException();
    }

    /**
     * {@code EmailVerificationTokenGenerator}에서 생성된 토큰으로 데이터베이스에 등록된 인증 정보를 찾는 테스트.
     * */
    @Test
    void emailService_findByToken_Success() {
        assertThatCode(() -> {
            EmailValidateUtil.isValid(testEmail);
            String token = emailVerificationTokenGenerator.generateEmailVerificationToken(testEmail);

            emailService.findByToken(token);
        }).doesNotThrowAnyException();
    }
}