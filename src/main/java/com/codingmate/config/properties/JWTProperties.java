package com.codingmate.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JWT 토큰 프로퍼티를 가지는 클래스
 *
 * @author duskafka
 * */
@ConfigurationProperties(prefix = "jwt")
public record JWTProperties(
        //JWT 토큰이 HTTP 헤더에 담길 때 사용될 헤더 이름 (예: Authorization)
        String accessTokenHeader,

        //Refresh Token 쿠키의 이름 (예: refresh-token)
        String refreshTokenCookie,

        //Refresh Token의 만료 기간 (일 단위)
        int refreshTokenExpirationDays,

        //JWT 서명 및 검증에 사용될 비밀 키 (Base64 인코딩된 문자열)
        //HS512 알고리즘을 사용할 것이기 때문에 512bit, 즉 64byte 이상의 secret key를 사용해야 합니다.
        String secret,

        //Access Token의 만료 기간 (초 단위)
        long accessTokenValidityInHour,

        //인증용 키
        String authorityKey,

        Redis redis

) {
    public record Redis(
            int maxToken,
            Key key
    ) {
        public record Key(
                String prefix,
                String suffix
        ) {}
    }
}