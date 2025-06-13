package com.codingmate.jwt;

import com.codingmate.common.annotation.Explanation;
import com.codingmate.config.properties.JWTProperties;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.jwt.ExpiredTokenException;
import com.codingmate.exception.exception.jwt.IllegalTokenException;
import com.codingmate.exception.exception.jwt.TokenSecutiryException;
import com.codingmate.exception.exception.jwt.UnsupportedTokenException;
import com.codingmate.refreshtoken.dto.response.RefreshTokenIssueResponse;
import com.codingmate.util.DateUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Explanation(
        responsibility = "JWT 토큰 생성",
        detail = "토큰 발급 외에 토근 검증도 한다",  //클래스 분리 요함
        domain = "Authority",
        lastReviewed = "2025.06.05"
)
public class TokenProvider {
    private final String AUTHORITIES_KEY;           // JWT 클레임에서 권한 정보를 추출할 때 사용하는 키
    private final Key KEY;                          // JWT 서명에 사용되는 Secret Key

    private final long ACCESS_TOKEN_VALIDATE_HOUR;     // 토큰의 유효 시간 (시간 단위)
    private final long REFRESH_TOKEN_VALIDATE_DAY;

    public TokenProvider(
            JWTProperties jwtProperties
    ) {
        this.AUTHORITIES_KEY = jwtProperties.authorityKey();
        this.ACCESS_TOKEN_VALIDATE_HOUR = jwtProperties.accessTokenValidityInHour();
        this.REFRESH_TOKEN_VALIDATE_DAY = jwtProperties.refreshTokenExpirationDays();
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secret());    // Secret 값을 Base64 디코딩하여 HMAC SHA 키로 변환합니다.
        this.KEY = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 사용자 ID와 역할을 포함하는 Access Token을 생성합니다.
     * 클레임(claims)에는 사용자 ID와 권한 정보가 포함되며, JWT 만료 시간 등이 설정됩니다.
     *
     * @param userId Access Token에 포함될 사용자 ID.
     * @param role Access Token에 포함될 사용자 역할 (권한).
     * @return 생성된 Access Token 문자열.
     */
    public String createAccessToken(Long userId, String role) {
        log.debug("[TokenProvider] createAccessToken({}, {})", userId, role);

        Instant issuedAt = Instant.now();
        Claims claims = createAccessTokenClaims(userId, role);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(issuedAt.plus(Duration.ofHours(ACCESS_TOKEN_VALIDATE_HOUR))))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();

        log.info("[TokenProvider] Access Token created for userId: {}", userId);
        return token;
    }

    private Claims createAccessTokenClaims(Long userId, String role) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
        claims.put(AUTHORITIES_KEY, role);
        claims.put("id", userId);
        return claims;
    }


    /**
     * 사용자 ID를 포함하는 Refresh Token을 생성합니다.
     * Refresh Token은 주로 Access Token의 갱신에 사용됩니다.
     *
     * @param userId Refresh Token에 포함될 사용자 ID.
     * @return 생성된 Refresh Token 문자열.
     */
    public RefreshTokenIssueResponse createRefreshToken(Long userId) {
        log.debug("[TokenProvider] createRefreshToken({})", userId);

        Instant issuedAt = Instant.now();
        String jti = UUID.randomUUID().toString();

        Claims claims = createRefreshTokenClaims(userId, jti);

        Date issuedAtDate = Date.from(issuedAt);
        Date expirationDate = Date.from(issuedAt.plus(Duration.ofDays(REFRESH_TOKEN_VALIDATE_DAY)));

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAtDate)
                .setExpiration(expirationDate)
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();

        log.info("[TokenProvider] Refresh Token created for userId: {}. Token length: {}", userId, refreshToken.length());

        return RefreshTokenIssueResponse.of(refreshToken, jti, issuedAt);
    }

    private Claims createRefreshTokenClaims(Long userId, String jti) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
        claims.put("jti", jti);
        return claims;
    }

    /**
     * JWT 토큰에서 인증(Authentication) 정보를 추출합니다.
     * 토큰을 파싱하여 클레임에서 사용자 ID와 권한을 얻어 Spring Security의 {@link Authentication} 객체를 생성합니다.
     *
     * @param token 인증 정보를 추출할 JWT 토큰.
     * @return 토큰에서 파싱된 {@link Authentication} 객체.
     */
    public Authentication getAuthentication(String token) {
        log.debug("[TokenProvider] getAuthentication({})", token);
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 클레임에서 권한 정보 추출 및 GrantedAuthority 객체로 변환
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);
        log.debug("[TokenProvider] Authentication principal created: Subject={}, Authorities={}", claims.getSubject(), authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * 주어진 JWT 토큰의 유효성을 검증합니다.
     * 토큰의 서명, 만료 여부, 지원되는 형식 등을 확인하며,
     * 유효하지 않을 경우 각 오류 유형에 해당하는 커스텀 예외를 발생시킵니다.
     *
     * @param token 유효성을 검증할 JWT 토큰.
     * @return 토큰이 유효하면 {@code true}를 반환합니다.
     * @throws TokenSecutiryException 잘못된 JWT 서명이거나 보안 문제가 있을 경우 발생.
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
            throw new TokenSecutiryException(ErrorMessage.SECURITY_TOKEN, "Invalid JWT signature or malformed token.");
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