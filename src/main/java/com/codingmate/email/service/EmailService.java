package com.codingmate.email.service;

import com.codingmate.email.dto.EmailVerificationTokenResponse;
import com.codingmate.email.repository.EmailVerificationTokenRepository;
import com.codingmate.email.repository.EmailVerificationTokenWriteRepository;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.email.NotFoundEmailVerificationTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailVerificationTokenRepository emailRepository;
    private final EmailVerificationTokenWriteRepository emailTokenRepository;

    @Transactional(readOnly = true)
    public EmailVerificationTokenResponse findByToken(String token) {
        return emailRepository.findByToken(token)
                .map(EmailVerificationTokenResponse::of)
                .orElseThrow(() -> new NotFoundEmailVerificationTokenException(
                        ErrorMessage.NOT_FOUND_EMAIL_VERIFICATION,
                        String.format("요청한 토큰(%s)로 데이터베이스에서 이메일 인증 토큰 조회를 실패했습니다", token)
                ));
    }

    @Transactional
    public void saveEmailVerificationToken(Long id) {
        long l = emailTokenRepository.verificationToken(id);
    }
}