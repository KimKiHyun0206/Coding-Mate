package com.codingmate.refreshtoken.dto.response;

import com.codingmate.refreshtoken.domain.RefreshToken;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.Instant;


/**
 * 리프레쉬 토큰이 저장되었음을 응답하는 DTO
 *
 * <li>내부에서만 사용하는 DTO로 외부 클라이언트에 응답하지 않는다</li>
 * <li>id: 리프레쉬 토큰의 PK</li>
 * <li>token: 리프레쉬 토큰 전문</li>
 * <li>jti: 리프레쉬 토큰의 jti</li>
 * <li>issuedAt: 리프레쉬 토큰의 발급 시간</li>
 * <li>expiredAt: 리프레쉬 토큰의 유효 시간</li>
 * <li>isRevoked: 리프레쉬 토큰이 사용되어서 유효한지</li>
 * <li>username: 리프레쉬 토큰의 사용자 아이디</li>
 *
 * @author duskafka
 * */
@Builder(access = AccessLevel.PRIVATE)
public record RefreshTokenResponse(
        Long id,
        String token,
        String jti,
        Instant issuedAt,
        Instant expiresAt,
        boolean isRevoked,
        String username
) {
    public static RefreshTokenResponse of(RefreshToken refreshToken) {
        return RefreshTokenResponse.builder()
                .id(refreshToken.getId())
                .token(refreshToken.getToken())
                .jti(refreshToken.getJti())
                .issuedAt(refreshToken.getIssuedAt())
                .expiresAt(refreshToken.getExpiresAt())
                .isRevoked(refreshToken.isRevoked())
                .username(refreshToken.getUsername())
                .build();
    }
}