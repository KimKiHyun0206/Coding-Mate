package com.codingmate.auth.service;

import com.codingmate.auth.dto.response.TokenResponse;
import com.codingmate.jwt.TokenProvider;
import com.codingmate.refreshtoken.dto.response.RefreshTokenIssueResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * 로그인 후 액세스 토큰과 리프레쉬 토큰을 생성하기 위한 서비스
 *
 * @author duskafka
 * */
@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenService {
    private final TokenProvider tokenProvider;

    /**
     * 사용자 ID와 권한을 기반으로 Access Token과 Refresh Token을 생성합니다.
     *
     * @return 생성된 Access Token과 Refresh Token을 담은 TokenDto
     */
    public TokenResponse generateToken(Authentication authentication) {
        String username = authentication.getName();
        log.debug("[TokenService] generateToken({})", username);

        String accessToken = tokenProvider.createAccessToken(authentication);
        RefreshTokenIssueResponse ref = tokenProvider.createRefreshToken(authentication.getName());

        log.info("[TokenService] 토큰이 생성되었습니다: username={}", username);

        return new TokenResponse(
                accessToken,
                ref.refreshToken(),
                ref.jti(),
                ref.issuedAt()
        );
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
        log.debug("[TokenService] validateToken({})", token);
        try {
            tokenProvider.validateToken(token);
            log.info("[TokenService] 토큰 검증이 성공적으로 처리되었습니다.");
        } catch (Exception e) {
            log.warn("[TokenService] 토큰 검증에 실패했습니다.: {}", e.getMessage()); // 예외 메시지 포함
            throw e; // 예외를 다시 던져 상위 계층에서 처리하도록 함
        }
    }
}