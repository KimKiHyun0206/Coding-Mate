package com.codingmate.jwt;

import com.codingmate.auth.domain.Authority;
import com.codingmate.config.properties.JWTProperties;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.jwt.ExpiredTokenException;
import com.codingmate.exception.exception.jwt.IllegalTokenException;
import com.codingmate.exception.exception.jwt.TokenSecurityException;
import com.codingmate.exception.exception.jwt.UnsupportedTokenException;
import com.codingmate.refreshtoken.dto.response.RefreshTokenIssueResponse;
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

/**
 * 토큰 발급을 위한 서비스
 *
 * @author duskafka
 * */
@Slf4j
@Component
public class TokenProvider {
    private final String AUTHORITIES_KEY;           // JWT 클레임에서 권한 정보를 추출할 때 사용하는 키
    private final Key KEY;                          // JWT 서명에 사용되는 Secret Key

    private final long ACCESS_TOKEN_VALIDATE_HOUR;     // 토큰의 유효 시간 (시간 단위)
    private final long REFRESH_TOKEN_VALIDATE_DAY;

    public TokenProvider(
            JWTProperties jwtProperties
    ) {
        this.AUTHORITIES_KEY = jwtProperties.authorityKey();
        this.ACCESS_TOKEN_VALIDATE_HOUR = Duration.ofHours(jwtProperties.accessTokenValidityInHour()).toMillis();
        this.REFRESH_TOKEN_VALIDATE_DAY = jwtProperties.refreshTokenExpirationDays();
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secret());    // Secret 값을 Base64 디코딩하여 HMAC SHA 키로 변환합니다.
        this.KEY = Keys.hmacShaKeyFor(keyBytes);
    }


    public String createAccessToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        claims.put("auth", roles);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + ACCESS_TOKEN_VALIDATE_HOUR);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, KEY)
                .compact();
    }


    /**
     * 사용자 ID를 포함하는 Refresh Token을 생성합니다.
     * Refresh Token은 주로 Access Token의 갱신에 사용됩니다.
     *
     * @param username Refresh Token에 포함될 사용자 ID.
     * @return 생성된 Refresh Token 문자열.
     */
    public RefreshTokenIssueResponse createRefreshToken(String username) {
        log.debug("[TokenProvider] createRefreshToken({})", username);

        Instant issuedAt = Instant.now();
        String jti = UUID.randomUUID().toString();

        Claims claims = createRefreshTokenClaims(username, jti);

        Date issuedAtDate = Date.from(issuedAt);
        Date expirationDate = Date.from(issuedAt.plus(Duration.ofDays(REFRESH_TOKEN_VALIDATE_DAY)));

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAtDate)
                .setExpiration(expirationDate)
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();

        log.info("[TokenProvider] Refresh Token created for username: {}. Token length: {}", username, refreshToken.length());

        return RefreshTokenIssueResponse.of(refreshToken, jti, issuedAt);
    }

    private Claims createRefreshTokenClaims(String username, String jti) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(username));
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
}