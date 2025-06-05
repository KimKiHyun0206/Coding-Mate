package com.codingmate.refreshtoken.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.time.Instant;
import java.util.Date;

@Builder(access = AccessLevel.PRIVATE)
public record RefreshTokenIssueResponse(
        String refreshToken,
        String jti,
        Instant issuedAt
){
    public static RefreshTokenIssueResponse of(String refreshToken, String jti, Instant issuedAt) {
        return RefreshTokenIssueResponse.builder()
                .refreshToken(refreshToken)
                .jti(jti)
                .issuedAt(issuedAt)
                .build();
    }
}
