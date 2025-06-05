package com.codingmate.refreshtoken.dto.response;

import com.codingmate.refreshtoken.domain.RefreshToken;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.Instant;

@Builder(access = AccessLevel.PRIVATE)
public record RefreshTokenResponse(
        Long id,
        String token,
        String jti,
        Instant issuedAt,
        Instant expiresAt,
        boolean isRevoked,
        Long userId
) {
    public static RefreshTokenResponse of(RefreshToken refreshToken) {
        return RefreshTokenResponse.builder()
                .id(refreshToken.getId())
                .token(refreshToken.getToken())
                .jti(refreshToken.getJti())
                .issuedAt(refreshToken.getIssuedAt())
                .expiresAt(refreshToken.getExpiresAt())
                .isRevoked(refreshToken.isRevoked())
                .userId(refreshToken.getUserId())
                .build();
    }
}