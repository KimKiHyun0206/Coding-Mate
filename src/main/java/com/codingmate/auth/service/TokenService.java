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
    private final TokenValidator tokenValidator;

    /**
     * {@code Authentication}기반으로 토큰 생성
     *
     * <li>액세스 토큰: 권한과 username으로 토큰 발급</li>
     * <li>리프레쉬 토큰: jti, 유효기간, 발급 시간으로 토큰 발급</li>
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
     * 매개변수로 들어온 JWT 토큰의 유효성을 검증한다. 만약 유효하지 않은 토큰이라면 예외를 던진다.
     *
     * @param token 검증할 JWT 문자열
     * @throws io.jsonwebtoken.security.SecurityException 잘못된 JWT 서명일 경우 발생합니다.
     * @throws io.jsonwebtoken.ExpiredJwtException JWT 토큰의 유효 기간이 만료되었을 경우 발생합니다.
     * @throws io.jsonwebtoken.UnsupportedJwtException 지원되지 않는 형식의 JWT 토큰일 경우 발생합니다.
     * @throws java.lang.IllegalArgumentException JWT 토큰이 null이거나, 비어있거나, 기타 유효하지 않은 인자일 경우 발생합니다.
     *
     */
    public void validateToken(String token) {
        log.debug("[TokenService] validateToken({})", token);
        try {
            tokenValidator.validateToken(token);
            log.info("[TokenService] 토큰 검증이 성공적으로 처리되었습니다.");
        } catch (Exception e) {
            log.warn("[TokenService] 토큰 검증에 실패했습니다.: {}", e.getMessage()); // 예외 메시지 포함
            throw e; // 예외를 다시 던져 상위 계층에서 처리하도록 함
        }
    }
}