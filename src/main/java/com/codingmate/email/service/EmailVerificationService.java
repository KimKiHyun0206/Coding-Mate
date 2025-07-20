package com.codingmate.email.service;

import com.codingmate.email.domain.EmailVerificationToken;
import com.codingmate.email.repository.EmailVerificationTokenRepository;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.email.NotFoundEmailVerificationTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Transactional(readOnly = true)
    public boolean isVerified(String email) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundEmailVerificationTokenException(
                        ErrorMessage.NOT_FOUND_EMAIL_VERIFICATION,
                        String.format("요청한 이메일(%s)을 데이터베이스에서 조회하지 못했습니다", email)
                ));
        return verificationToken.isVerified();
    }
}