package com.codingmate.jwt;

import com.codingmate.config.properties.JWTProperties;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.jwt.ExpiredTokenException;
import com.codingmate.exception.exception.jwt.IllegalTokenException;
import com.codingmate.exception.exception.jwt.TokenSecurityException;
import com.codingmate.exception.exception.jwt.UnsupportedTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;

/**
 * 토큰의 유효성을 검증하기 위한 클래스
 *
 * @author duskafka
 * */
@Slf4j
@Service
public class TokenValidator {
    private final Key KEY;

    public TokenValidator(JWTProperties jwtProperties) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secret());    // Secret 값을 Base64 디코딩하여 HMAC SHA 키로 변환합니다.
        this.KEY = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 주어진 JWT 토큰의 유효성을 검증합니다.
     * 토큰의 서명, 만료 여부, 지원되는 형식 등을 확인하며,
     * 유효하지 않을 경우 각 오류 유형에 해당하는 커스텀 예외를 발생시킵니다.
     *
     * @param token 유효성을 검증할 JWT 토큰.
     * @return 토큰이 유효하면 {@code true}를 반환합니다.
     * @throws TokenSecurityException 잘못된 JWT 서명이거나 보안 문제가 있을 경우 발생.
     * @throws ExpiredTokenException JWT 토큰의 유효 기간이 만료되었을 경우 발생.
     * @throws UnsupportedTokenException 지원되지 않는 형식의 JWT 토큰일 경우 발생.
     * @throws IllegalTokenException JWT 토큰이 잘못되었거나 유효하지 않은 인자를 포함할 경우 발생.
     */
    public boolean validateToken(String token) {
        try {
            log.debug("[TokenProvider] validateToken({})", token);
            Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token); // 토큰 파싱 및 검증
            log.info("[TokenProvider] Token validation successful.");
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.warn("[TokenProvider] Invalid JWT signature or malformed JWT token. Token: {}, Error: {}", token, e.getMessage());
            throw new TokenSecurityException(ErrorMessage.SECURITY_TOKEN, "Invalid JWT signature or malformed token.");
        } catch (ExpiredJwtException e) {
            log.warn("[TokenProvider] Expired JWT token. Token: {}, Error: {}", token, e.getMessage());
            throw new ExpiredTokenException(ErrorMessage.EXPIRED_JWT, "JWT token has expired.");
        } catch (UnsupportedJwtException e) {
            log.warn("[TokenProvider] Unsupported JWT token. Token: {}, Error: {}", token, e.getMessage());
            throw new UnsupportedTokenException(ErrorMessage.INVALID_JWT, "Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.warn("[TokenProvider] JWT token is illegal or invalid argument. Token: {}, Error: {}", token, e.getMessage());
            throw new IllegalTokenException(ErrorMessage.INVALID_JWT, "Illegal or invalid JWT token.");
        }
    }
}
