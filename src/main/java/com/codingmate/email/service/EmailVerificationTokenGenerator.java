package com.codingmate.email.service;

import com.codingmate.email.domain.EmailVerificationToken;
import com.codingmate.email.repository.EmailVerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationTokenGenerator {
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Transactional
    public String generateEmailVerificationToken(String email) {
        log.debug("[EmailVerificationTokenGenerator] generateEmailVerificationToken({})", email);
        EmailVerificationToken verificationToken = new EmailVerificationToken(email);
        emailVerificationTokenRepository.save(verificationToken);

        log.info("[EmailVerificationTokenGenerator] 이메일 인증을 위한 토큰이 제대로 발급되었습니다: email={}, token={}", email, verificationToken.getToken());
        return verificationToken.getToken();
    }
}