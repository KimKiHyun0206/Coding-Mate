package com.codingmate.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 액세스 토큰의 유효 시간이 제대로 적용되는지 테스트하기 위한 테스트 클래스.
 *
 * @author duskafka
 * */
@SpringBootTest
@ActiveProfiles("test-jwt")
public class JwtTest {
    private final TokenProvider tokenProvider;
    private final TokenValidator tokenValidator;

    private final String TEST_USERNAME = "username";
    private final String TEST_PASSWORD = "password";

    @Autowired
    public JwtTest(TokenProvider tokenProvider, TokenValidator tokenValidator) {
        this.tokenProvider = tokenProvider;
        this.tokenValidator = tokenValidator;
    }

    /**
     * Authentication 객체를 만들어주는 빌더 메소드.
     * */
    private Authentication getAuthentication() {
        return new UsernamePasswordAuthenticationToken(
                TEST_USERNAME,
                TEST_PASSWORD,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }


    /**
     * 유효 기간이 지나지 않아서 토큰 검증에 성공하는 테스트 코드. 설정에서 토큰 유효 기간을 3시간으로 설정하였다.
     * */
    @Test
    void tokenValidator_validateToken_Success(){
        // Given
        String accessToken = tokenProvider.createAccessToken(getAuthentication());

        // Then
        assertThat(accessToken).isNotEmpty();
        assertThat(accessToken).isNotBlank();
        assertTrue(tokenValidator.validateToken(accessToken));
    }

    /**
     * 토큰을 파싱했을 때 내부에 제대로 정보가 들어가있는지 테스트하는 코드.
     * */
    @Test
    void parseToken_Success() {
        // Given
        String accessToken = tokenProvider.createAccessToken(getAuthentication());
        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        // Then
        assertThat(authentication).isNotNull();
        assertThat(authentication).isInstanceOf(UsernamePasswordAuthenticationToken.class);
        assertThat(authentication.getName()).isNotNull();
        assertThat(authentication.getName()).isEqualTo(TEST_USERNAME);
        assertThat(authentication.getAuthorities()).isNotNull();
        assertThat(authentication.getAuthorities()).hasSize(1);
        assertThat(authentication.getAuthorities().iterator().next().getAuthority()).isEqualTo("[ROLE_USER]");
        assertThat(authentication.getCredentials()).isNotNull();
        assertThat(authentication.getPrincipal()).isNotNull();
    }
}
