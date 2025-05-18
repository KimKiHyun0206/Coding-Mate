package com.codingMate.jwt;

import com.codingMate.exception.dto.ErrorMessage;
import com.codingMate.exception.exception.jwt.ExpiredTokenException;
import com.codingMate.exception.exception.jwt.IllegalTokenException;
import com.codingMate.exception.exception.jwt.TokenSecutiryException;
import com.codingMate.exception.exception.jwt.UnsupportedTokenException;
import com.codingMate.service.redis.RefreshTokenService;
import com.codingMate.util.DateUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long tokenValidityInMilliseconds;
    private Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds
    ) {
        this.secret = secret;
        //this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        this.tokenValidityInMilliseconds = 10000;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * @param * claim : 데이터 (String userId)
     *          * Subject : 토큰 생성 목적
     *          * IssuedAt : jwt 발급 시간
     *          * Expiration Time : jwt 만료시간
     *          * signWith : 암호화 알고리즘, secret 값 세팅
     *          *
     * @return * access Jwt Token
     * @brief * 토큰 생성 / jjwt 라이브러리
     */
    public String createAccessToken(Long userId, String role) {
        var claims = Jwts.claims().setSubject(String.valueOf(userId));
        claims.put(AUTHORITIES_KEY, role);
        claims.put("id", userId);
        var date = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .signWith(SignatureAlgorithm.HS256, key)
                .setExpiration(DateUtil.getTokenValidTime(date, tokenValidityInMilliseconds))
                .compact();
    }

    public String createRefreshToken(Long userId) {
        var claims = Jwts.claims().setSubject(String.valueOf(userId));
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(DateUtil.getTokenValidTime(now, tokenValidityInMilliseconds))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }


    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다 {}", token);
            throw new TokenSecutiryException(ErrorMessage.SECURITY_TOKEN, "잘못된 JWT 서명입니다");
        } catch (ExpiredJwtException e) {
            log.info("유효기간이 지난 JWT 토큰입니다 {}", token);
            throw new ExpiredTokenException(ErrorMessage.EXPIRED_JWT, "유효기간이 지난 JWT 토큰입니다");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다 {}", token);
            throw new UnsupportedTokenException(ErrorMessage.INVALID_JWT, "지원되지 않는 JWT 토큰입니다");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다 {}", token);
            throw new IllegalTokenException(ErrorMessage.INVALID_JWT, "JWT 토큰이 잘못되었습니다");
        }
    }
}