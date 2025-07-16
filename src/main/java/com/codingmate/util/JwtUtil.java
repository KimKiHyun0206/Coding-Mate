package com.codingmate.util;

import com.codingmate.config.properties.JWTProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 헤더에서 토큰 정보를 가져온다
 *
 * @author duskafka
 * */
@Slf4j
@Component
public class JwtUtil {
    private static String SECRET;
    private static String ACCESS_TOKEN_HEADER;

    public JwtUtil(
            JWTProperties jwtProperties
    ) {
        JwtUtil.SECRET = jwtProperties.secret();
        JwtUtil.ACCESS_TOKEN_HEADER = jwtProperties.accessTokenHeader();
    }

    public static Long getId(HttpServletRequest request) {
        String token = request.getHeader(ACCESS_TOKEN_HEADER);
        if (token == null || token.equals("null")) return null;
        return Long.parseLong(getAllClaims(token).get("id").toString());
    }

    public static Long getId(String token) {
        return (Long) getAllClaims(token).get("id");
    }

    public static String getUsername(String refreshToken) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(refreshToken)
                .getBody()
                .getSubject();

    }

    public static String getJti(String refreshToken) {
        return getAllClaims(refreshToken).get("jti", String.class);
    }


    public static String getAccessToken(HttpServletRequest request) {
        String token = request.getHeader(ACCESS_TOKEN_HEADER);
        if (token == null || token.equals("null")) return null;
        return token;
    }

    /**
     * 토큰의 Claim 디코딩
     */
    private static Claims getAllClaims(String token) {
        log.info("getAllClaims token = {}", token);
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}