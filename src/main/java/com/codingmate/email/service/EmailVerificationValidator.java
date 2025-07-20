package com.codingmate.email.service;

import com.codingmate.email.dto.EmailVerificationTokenResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 이메일 인증이 유효한지 검사해주는 검사 클래스
 *
 * <li>이미 인증된 것인지 체크한다</li>
 * <li>유효 기간이 지나지 않았는지 체크한다.</li>
 * */
@Service
public class EmailVerificationValidator {
    public boolean isValidEmailToken(EmailVerificationTokenResponse verification) {
        if (verification.verified()) {
            return false;
        }

        return !verification.expiry().isBefore(LocalDateTime.now());
    }
}