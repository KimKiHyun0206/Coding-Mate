package com.codingmate.refreshtoken.dto.request;

import com.codingmate.refreshtoken.dto.response.RefreshTokenIssueResponse;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.Instant;

@Builder(access = AccessLevel.PRIVATE)
public record RefreshTokenCreateRequest(
        String token,
        String jti,
        Instant issuedAt,
        Long userId
) {
    public static RefreshTokenCreateRequest of(String token, String jti, Instant issuedAt, Long userId) {
        return RefreshTokenCreateRequest.builder()
                .token(token)
                .jti(jti)
                .issuedAt(issuedAt)
                .userId(userId)
                .build();
    }

    public static RefreshTokenCreateRequest of(
            RefreshTokenIssueResponse refreshToken,
            Long id
    ) {

        return RefreshTokenCreateRequest.builder()
                .token(refreshToken.refreshToken())
                .jti(refreshToken.jti())
                .issuedAt(refreshToken.issuedAt())
                .userId(id)
                .build();
    }
}