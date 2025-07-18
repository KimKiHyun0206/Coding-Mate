package com.codingmate.email.dto;

import com.codingmate.email.domain.EmailVerificationToken;

import java.time.LocalDateTime;

public record EmailVerificationTokenResponse(
        Long id,
        String email,
        String token,
        LocalDateTime expiry,
        boolean verified
) {
    public static EmailVerificationTokenResponse of(EmailVerificationToken token) {
        return new EmailVerificationTokenResponse(
                token.getId(),
                token.getEmail(),
                token.getToken(),
                token.getExpiry(),
                token.isVerified()
        );
    }
}