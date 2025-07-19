package com.codingmate.controller.api;

import com.codingmate.email.dto.EmailVerificationTokenResponse;
import com.codingmate.email.service.EmailSendService;
import com.codingmate.email.service.EmailService;
import com.codingmate.email.service.EmailVerificationTokenGenerator;
import com.codingmate.email.service.EmailVerificationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 이메일 인증을 실행해줄 컨트롤러.
 *
 * @author duskafka
 */
@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    private final EmailVerificationValidator emailVerificationValidator;
    private final EmailSendService emailSendService;
    private final EmailVerificationTokenGenerator emailVerificationTokenGenerator;

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        EmailVerificationTokenResponse verification = emailService.findByToken(token);

        boolean isEmailVerificationValid = emailVerificationValidator.isValidEmailToken(verification);

        return isEmailVerificationValid ?
                ResponseEntity.ok(HttpStatus.OK) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> sendEmail(@RequestParam("email") String email) {
        String token = emailVerificationTokenGenerator.generateEmailVerificationToken(email);
        emailSendService.sendHtmlEmail(email, token);
        return ResponseEntity.ok().build();
    }
}