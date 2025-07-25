package com.codingmate.jwt;

import com.codingmate.config.properties.JWTProperties;
import com.codingmate.exception.exception.jwt.ExpiredTokenException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 액세스 토큰이 시간이 지나면 만료되는지 테스트하기 위한 테스트 클래스.
 *
 * @author duskafka
 * */
@Slf4j
@SpringBootTest
@ActiveProfiles("test-jwt-expired")
@EnableConfigurationProperties(JWTProperties.class)
public class JwtExpireTest {

    private final TokenProvider tokenProvider;
    private final TokenValidator tokenValidator;

    private final String TEST_USERNAME = "username";
    private final String TEST_PASSWORD = "password";

    @Autowired
    public JwtExpireTest(TokenProvider tokenProvider, TokenValidator tokenValidator) {
        this.tokenProvider = tokenProvider;
        this.tokenValidator = tokenValidator;
    }

    /**
     * Authentication 객체를 만들어주는 빌더 메소드
     * */
    private Authentication getAuthentication() {
        return new UsernamePasswordAuthenticationToken(
                TEST_USERNAME,
                TEST_PASSWORD,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    /**
     * 액세스 토큰이 만료되어 ExpiredTokenException을 던지도록 구현된 테스트 코드. 설정 파일에서 유효기간을 0으로 설정해 발급과 동시에 무효되도록 함.
     * */
    @Test
    void tokenValidator_validateToken_Fail_WhenTokenExpired(){
        // Given
        String accessToken = tokenProvider.createAccessToken(getAuthentication());

        // Then
        assertThat(accessToken).isNotEmpty();
        assertThat(accessToken).isNotBlank();
        assertThrows(
                ExpiredTokenException.class,
                () -> tokenValidator.validateToken(accessToken)
        );
    }
}