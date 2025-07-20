package com.codingmate.controller.api;

import com.codingmate.email.dto.EmailVerificationTokenResponse;
import com.codingmate.email.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    private final EmailVerificationService emailVerificationService;

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        EmailVerificationTokenResponse verification = emailService.findByToken(token);

        boolean isEmailVerificationValid = emailVerificationValidator.isValidEmailToken(verification);
        if (isEmailVerificationValid) {
            emailVerificationService.verify(token);
            return ResponseEntity.status(HttpStatus.SEE_OTHER)
                    .header(HttpHeaders.LOCATION, "http://localhost:8080/verification-result?success=true")
                    .build();
        }

        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .header(HttpHeaders.LOCATION, "http://localhost:8080/verification-result?success=false")
                .build();
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> sendEmail(@RequestParam("email") String email) {
        String token = emailVerificationTokenGenerator.generateEmailVerificationToken(email);
        emailSendService.sendHtmlEmail(email, token);
        return ResponseEntity.ok().build();
    }
}