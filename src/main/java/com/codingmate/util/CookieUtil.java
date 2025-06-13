package com.codingmate.util;

import com.codingmate.common.annotation.Explanation;
import com.codingmate.config.properties.JWTProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Explanation(
        responsibility = "쿠키 생성 및 삭제",
        lastReviewed = "2025.06.05"
)
public class CookieUtil {
    private static final long ONE_DAY_IN_SECONDS = 24 * 60 * 60;
    private static int REFRESH_TOKEN_EXPIRE_DAY;

    public CookieUtil(JWTProperties jwtProperties) {
        CookieUtil.REFRESH_TOKEN_EXPIRE_DAY = jwtProperties.refreshTokenExpirationDays();
    }

    public static ResponseCookie getCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)       // JavaScript 접근 불가
                .secure(true)         // HTTPS 에서만 전송
                .path("/")            // 모든 경로에서 쿠키 유효
                .maxAge(REFRESH_TOKEN_EXPIRE_DAY * ONE_DAY_IN_SECONDS)
                .sameSite("Lax")      // CSRF 방어 (Strict, Lax, None)
                .build();
    }

    public static ResponseCookie deleteCookie(String name){
        return ResponseCookie.from(name, "") // 쿠키 이름은 클라이언트에 저장된 이름과 동일하게
                .httpOnly(true)       // HttpOnly, Secure, Path 등 기존 쿠키와 동일한 속성 설정
                .secure(true)         // HTTPS 환경에서는 true
                .path("/")            // 쿠키가 설정된 경로와 동일하게
                .maxAge(0)            // Max-Age를 0으로 설정하여 쿠키를 즉시 만료시킴
                .sameSite("Lax")      // 기존 SameSite 속성과 동일하게
                .build();
    }
}