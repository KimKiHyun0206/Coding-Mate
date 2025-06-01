package com.codingmate.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {
    private static String SECRET;
    private static String ACCESS_TOKEN_HEADER;

    public JwtUtil(
            @Value("${jwt.secret}") String SECRET,
            @Value("${jwt.header}") String header
    ) {
        JwtUtil.SECRET = SECRET;
        JwtUtil.ACCESS_TOKEN_HEADER = header;
    }

    public static Long getId(HttpServletRequest request) {
        String token = request.getHeader(ACCESS_TOKEN_HEADER);
        if (token == null || token.equals("null")) return null;
        return getIdFromString(token);
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

    /**
     * Claim 에서 username 가져오기
     */
    private static Long getIdFromString(String token) {
        String id = String.valueOf(getAllClaims(token).get("id"));
        log.info("getUsernameFromToken id = {}", id);
        return Long.valueOf(id);
    }
}