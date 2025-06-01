package com.codingmate.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseCookie;

@UtilityClass
public class CookieUtil {
    public ResponseCookie getCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)       // JavaScript 접근 불가
                .secure(true)         // HTTPS 에서만 전송
                .path("/")            // 모든 경로에서 쿠키 유효
                .maxAge(60 * 24 * 60 * 60) // 예: 60일 (초 단위)
                .sameSite("Lax")      // CSRF 방어 (Strict, Lax, None)
                .build();
    }

    public ResponseCookie deleteCookie(String name){
        return ResponseCookie.from(name, "") // 쿠키 이름은 클라이언트에 저장된 이름과 동일하게
                .httpOnly(true)       // HttpOnly, Secure, Path 등 기존 쿠키와 동일한 속성 설정
                .secure(true)         // HTTPS 환경에서는 true
                .path("/")            // 쿠키가 설정된 경로와 동일하게
                .maxAge(0)            // Max-Age를 0으로 설정하여 쿠키를 즉시 만료시킴
                .sameSite("Lax")      // 기존 SameSite 속성과 동일하게
                .build();
    }
}
