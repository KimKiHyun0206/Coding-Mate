package com.codingmate.auth.service;

import com.codingmate.auth.dto.response.TokenResponse;
import com.codingmate.common.annotation.Explanation;
import com.codingmate.jwt.TokenProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Explanation(
        responsibility = "토큰 생성",
        domain = "RefreshToken, Authority, Programmer",
        lastReviewed = "2025.06.05"
)
public class TokenService {
    private final TokenProvider tokenProvider;

    /**
     * 사용자 ID와 권한을 기반으로 Access Token과 Refresh Token을 생성합니다.
     *
     * @param id 토큰을 생성할 사용자의 ID
     * @param authority 토큰에 포함될 사용자의 권한
     * @return 생성된 Access Token과 Refresh Token을 담은 TokenDto
     */
    public TokenResponse generateToken(Long id, String authority) {
        log.debug("[TokenService] generateToken - Request to generate tokens for user ID: {}, authority: {}", id, authority);

        log.info("[TokenService] Generating new access and refresh tokens for user ID: {}", id);
        String accessToken = tokenProvider.createAccessToken(id, authority);
        var refreshTokenResult = tokenProvider.createRefreshToken(id);

        var tokenDto = new TokenResponse(
                accessToken,
                refreshTokenResult.refreshToken(),
                refreshTokenResult.jti(),
                refreshTokenResult.issuedAt()
        );

        log.info("[TokenService] Tokens successfully generated for user ID: {}", id);
        log.debug("[TokenService] Generated Access Token (prefix): {}, Refresh Token (prefix): {}",
                accessToken.substring(20), // 앞부분만 로깅
                refreshTokenResult.refreshToken().substring(20)
        ); // 앞부분만 로깅

        return tokenDto;
    }

    /**
     * 주어진 JWT 토큰의 유효성을 검증합니다.
     *
     * @param token 검증할 JWT 문자열
     * @throws io.jsonwebtoken.security.SecurityException 잘못된 JWT 서명일 경우 발생합니다.
     * @throws io.jsonwebtoken.ExpiredJwtException JWT 토큰의 유효 기간이 만료되었을 경우 발생합니다.
     * @throws io.jsonwebtoken.UnsupportedJwtException 지원되지 않는 형식의 JWT 토큰일 경우 발생합니다.
     * @throws java.lang.IllegalArgumentException JWT 토큰이 null이거나, 비어있거나, 기타 유효하지 않은 인자일 경우 발생합니다.
     */
    public void validateToken(String token) {
        log.info("[TokenService] Attempting to validate token.");

        log.debug("[TokenService] Validating token (prefix): {}", token.substring(0, Math.min(token.length(), 20)));
        try {
            tokenProvider.validateToken(token);
            log.info("[TokenService] Token validation successful.");
        } catch (Exception e) {
            log.warn("[TokenService] Token validation failed: {}", e.getMessage()); // 예외 메시지 포함
            throw e; // 예외를 다시 던져 상위 계층에서 처리하도록 함
        }
    }
}