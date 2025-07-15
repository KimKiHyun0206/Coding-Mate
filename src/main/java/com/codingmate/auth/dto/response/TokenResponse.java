package com.codingmate.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;

/**
 * 토큰에 대한 정보를 응답하기 위한 DTO.
 * <li>이 DTO는 응답에 쓰이는 것이 아니라 TokenProvider에서 컨트롤러로 응답하기 위한 DTO이다.</li>
 * <li>accessToken: 액세스 토큰</li>
 * <li>refreshToken: 리프레쉬 토큰</li>
 * <li>jti: 토큰의 JTI로 리프래쉬 토큰 블랙리스트에 사용</li>
 * <li>instant: 토큰이 발급된 시간</li>
 *
 * @author duskafka
 * */
@Builder
@Schema(description = "인증/인가 응답으로 전달되는 토큰 정보 DTO")
public record TokenResponse(
        @Schema(description = "액세스 토큰 (API 요청 시 사용)", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,

        @Schema(description = "리프레시 토큰 (액세스 토큰 재발급 시 사용)", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String refreshToken,

        @Schema(description = "리프레시 토큰의 jti")
        String jti,

        Instant instant

) {
    /**
     * accessToken과 refreshToken을 사용하여 TokenDto를 생성합니다.
     *
     * @param accessToken  새로 발급된 액세스 토큰
     * @param refreshToken 새로 발급된 리프레시 토큰
     * @return TokenDto 객체
     */
    public static TokenResponse of(String accessToken, String refreshToken, String jti, Instant instant) {
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .jti(jti)
                .instant(instant)
                .build();
    }
}