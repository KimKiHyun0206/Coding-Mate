package com.codingmate.email.service;

import com.codingmate.email.domain.EmailVerificationToken;
import com.codingmate.email.repository.EmailVerificationTokenRepository;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.email.DuplicateEmailVerificationException;
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

        if (emailVerificationTokenRepository.isExistsByEmail(email)) {
            throw new DuplicateEmailVerificationException(
                    ErrorMessage.DUPLICATE_EMAIL_VERIFICATION,
                    String.format("요청한 이메일(%s)는 인증이 진행 중이거나 인증이 완료된 이메일입니다.", email)
            );
        }

        EmailVerificationToken verificationToken = new EmailVerificationToken(email);
        emailVerificationTokenRepository.save(verificationToken);

        log.info("[EmailVerificationTokenGenerator] 이메일 인증을 위한 토큰이 제대로 발급되었습니다: email={}, token={}", email, verificationToken.getToken());
        return verificationToken.getToken();
    }
}