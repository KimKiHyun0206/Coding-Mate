package com.codingMate.util;

import com.codingMate.exception.exception.jwt.NoTokenInHeaderException;
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
    private static String header = "Coding-Mate-Auth";

    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.header}")String header) {
        JwtUtil.secret = secret;
        JwtUtil.header = header;
    }


    public static Long getIdFromToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        return getUsernameFromToken(token);
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
    public static Long getUsernameFromToken(String token) {
        String id = String.valueOf(getAllClaims(token).get("id"));
        log.info("getUsernameFromToken id = {}", id);
        return Long.valueOf(id);
    }
}