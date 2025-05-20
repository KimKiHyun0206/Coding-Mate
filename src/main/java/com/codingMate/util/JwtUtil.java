package com.codingMate.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {
    private static String secret;
    private static String header;
    private static String refresh;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.header}") String header,
            @Value("${jwt.refresh}") String refresh
    ) {
        JwtUtil.secret = secret;
        JwtUtil.header = header;
        JwtUtil.refresh = refresh;
    }

    public static Long getId(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (token == null || token.equals("null")) return null;
        return getIdFromString(token);
    }

    public static String getRefreshToken(HttpServletRequest request) {
        String token = request.getHeader(refresh);
        if (token == null || token.equals("null")) return null;
        return token;
    }

    public static String getAccessToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (token == null || token.equals("null")) return null;
        return token;
    }

    /**
     * 토큰의 Claim 디코딩
     */
    private static Claims getAllClaims(String token) {
        log.info("getAllClaims token = {}", token);
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Claim 에서 username 가져오기
     */
    private static Long getIdFromString(String token) {
        String id = String.valueOf(getAllClaims(token).get("id"));
        log.info("getUsernameFromToken id = {}", id);
        return Long.valueOf(id);
    }
}