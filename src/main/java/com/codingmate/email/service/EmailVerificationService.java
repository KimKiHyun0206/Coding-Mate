package com.codingmate.email.service;

import com.codingmate.email.domain.EmailVerificationToken;
import com.codingmate.email.repository.EmailVerificationTokenRepository;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.email.NotFoundEmailVerificationTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final EmailVerificationTransactionalHelper emailVerificationTransactionalHelper;

    @Transactional(readOnly = true)
    public boolean isVerified(String email) {
        log.debug("[EmailVerificationService] isVerified({})", email);
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundEmailVerificationTokenException(
                        ErrorMessage.NOT_FOUND_EMAIL_VERIFICATION,
                        String.format("요청한 이메일(%s)을 데이터베이스에서 조회하지 못했습니다", email)
                ));
        log.info("[EmailVerificationService] 이메일이 {}: email={}", verificationToken.isVerified() ? "인증 되었습니다." : "인증되지 않았습니다.", email);
        return verificationToken.isVerified();
    }

    @Transactional(readOnly = true)
    public void verify(String token) {
        log.debug("[EmailVerificationService] verify({})", token);
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token).orElseThrow(
                () -> new NotFoundEmailVerificationTokenException(
                        ErrorMessage.NOT_FOUND_EMAIL_VERIFICATION,
                        String.format("요청한 이메일(토큰:%s)을 데이터베이스에서 조회하지 못했습니다", token)
                ));
        emailVerificationTransactionalHelper.markAsVerified(verificationToken);
        log.info("[EmailVerificationService] 이메일 인증이 정상적으로 처리되었습니다: email={}", verificationToken.getEmail());
    }
}