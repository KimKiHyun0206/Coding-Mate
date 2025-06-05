package com.codingmate.refreshtoken.dto.request;

import lombok.AccessLevel;
import lombok.Builder;

import java.time.Instant;

@Builder(access = AccessLevel.PRIVATE)
public record RefreshTokenCreateRequest(
        String token,
        String jti,
        Instant issuedAt,
        Long userId) {
    public static RefreshTokenCreateRequest of(String token, String jti, Instant issuedAt, Long userId) {
        return RefreshTokenCreateRequest.builder()
                .token(token)
                .jti(jti)
                .issuedAt(issuedAt)
                .userId(userId)
                .build();
    }
}