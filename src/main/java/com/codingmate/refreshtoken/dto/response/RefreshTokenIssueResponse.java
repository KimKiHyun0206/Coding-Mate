package com.codingmate.refreshtoken.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.time.Instant;
import java.util.Date;

/**
 * 리프레쉬 토큰이 발급된 것을 응답하는 DTO
 *
 * <li>내부에서만 사용하는 DTO</li>
 * <li>refreshToken: 리프레쉬 토큰 전문</li>
 * <li>jti: 리프레쉬 토큰의 jti</li>
 * <li>issuedAt: 리프레쉬 토큰이 발급된 시간</li>
 *
 * @author duskafka
 * */
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
