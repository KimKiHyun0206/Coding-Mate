package com.codingmate.refreshtoken.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.time.Instant;

@Builder(access = AccessLevel.PRIVATE)
public record RefreshTokenResolveResponse(
        Long userId,
        String jti,
        Instant issuedAt
) {
    public static RefreshTokenResolveResponse of(Long userId) {
        return RefreshTokenResolveResponse.builder()
                .userId(userId)
                .build();
    }
}
