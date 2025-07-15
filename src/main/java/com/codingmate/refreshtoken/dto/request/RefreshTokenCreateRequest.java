package com.codingmate.refreshtoken.dto.request;

import com.codingmate.refreshtoken.dto.response.RefreshTokenIssueResponse;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.Instant;

/**
 * 리프레쉬 토큰 저장을 요청하는 DTO
 *
 * <li>외부에서 만들어지는 DTO가 아니라 애플리케이션 내부에서 리프레쉬 토큰이 발급되었을 때 데이터베이스에 저장하기 위해서 사용하는 DTO</li>
 * <li>token: 리프레쉬 토큰 전문</li>
 * <li>jti: 리프레쉬 토큰의 jti</li>
 * <li>issuedAt: 리프레쉬 토큰이 발급된 시간</li>
 * <li>username: 리프레쉬 토큰의 사용자 아이디</li>
 *
 * @author duskafka
 * */
@Builder(access = AccessLevel.PRIVATE)
public record RefreshTokenCreateRequest(
        String token,
        String jti,
        Instant issuedAt,
        String username
) {
    public static RefreshTokenCreateRequest of(String token, String jti, Instant issuedAt, String username) {
        return RefreshTokenCreateRequest.builder()
                .token(token)
                .jti(jti)
                .issuedAt(issuedAt)
                .username(username)
                .build();
    }

    public static RefreshTokenCreateRequest of(
            RefreshTokenIssueResponse refreshToken,
            String username
    ) {
        return RefreshTokenCreateRequest.builder()
                .token(refreshToken.refreshToken())
                .jti(refreshToken.jti())
                .issuedAt(refreshToken.issuedAt())
                .username(username)
                .build();
    }
}