package com.codingMate.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class JwtUtil {
    private static String secret;

    @Getter
    private static String header;

    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.header}") String header) {
        JwtUtil.secret = secret;
        JwtUtil.header = header;
    }


    public static Long getIdFromHttpServletRequest(HttpServletRequest request) {
        String token = request.getHeader(header);
        if(token == null) return null;
        return getIdFromString(token);
    }

    /**
     * 토큰의 Claim 디코딩
     */
    private static Claims getAllClaims(String token) {
        log.info("getAllClaims token = {}", token);
        if (token == null) return null;
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Claim 에서 username 가져오기
     */
    public static Long getIdFromString(String token) {
        String id = String.valueOf(getAllClaims(token).get("id"));
        log.info("getUsernameFromToken id = {}", id);
        return Long.valueOf(id);
    }
}