package com.codingmate.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTProperties {
    /**
     * JWT 토큰이 HTTP 헤더에 담길 때 사용될 헤더 이름 (예: Authorization)
     */
    private String accessTokenHeader;

    /**
     * Refresh Token 쿠키의 이름 (예: refresh-token)
     */
    private String refreshTokenCookie;

    /**
     * Refresh Token의 만료 기간 (일 단위)
     */
    private int expirationDays;

    /**
     * JWT 서명 및 검증에 사용될 비밀 키 (Base64 인코딩된 문자열)
     * HS512 알고리즘을 사용할 것이기 때문에 512bit, 즉 64byte 이상의 secret key를 사용해야 합니다.
     */
    private String secret;

    /**
     * Access Token의 만료 기간 (초 단위)
     */
    private long tokenValidityInSeconds;

    private String authorityKey;
}
