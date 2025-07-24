package com.codingmate.programmer;

import com.codingmate.exception.exception.jwt.NotFoundRefreshTokenException;
import com.codingmate.exception.exception.jwt.RefreshTokenIsRevokedException;
import com.codingmate.exception.exception.jwt.UnMatchJTIException;
import com.codingmate.exception.exception.redis.FailedFindRefreshTokenException;
import com.codingmate.jwt.TokenProvider;
import com.codingmate.programmer.repository.DefaultProgrammerRepository;
import com.codingmate.refreshtoken.dto.request.RefreshTokenCreateRequest;
import com.codingmate.refreshtoken.repository.RefreshTokenRepository;
import com.codingmate.refreshtoken.service.RefreshTokenService;
import com.codingmate.refreshtoken.service.validate.JtiValidator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

@Slf4j
@SpringBootTest
@ActiveProfiles("test") // 테스트 프로파일 활성화 (application-test.yml 로드)
public class RefreshTokenServiceTest {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    @Qualifier("stringRedisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private JtiValidator jtiValidator;

    private final String username = "test_username";

    /**
     * 데이터베이스의 리프레쉬 토큰 정보를 모두 삭제하고 Redis에서도 삭제한다.
     * */
    @BeforeEach
    void setUp() {
        refreshTokenRepository.deleteAll();

        String result = redisTemplate.execute((RedisConnection connection) -> {
            connection.flushDb();
            return "OK";
        }, true);
        log.info("Redis 초기화 결과: result={}", result);
    }

    // 토큰을 발급해주는 메소드
    private RefreshTokenCreateRequest createRequest() {
        var refreshToken = tokenProvider.createRefreshToken(username);
        return new RefreshTokenCreateRequest(
                refreshToken.refreshToken(),
                refreshToken.jti(),
                refreshToken.issuedAt(),
                username
        );
    }

    /**
     * 토큰 발급에 성공하는 테스트.
     * */
    @Test
    void create_Success() {
        assertThatCode(() -> refreshTokenService.create(createRequest()))
                .doesNotThrowAnyException();
    }

    /**
     * Jti가 사용되었는지 검증하는데 사용되지 않아서 성공하는 테스트.
     * */
    @Test
    void isUsedJti_Success() {
        var refreshTokenResponse = refreshTokenService.create(createRequest());

        assertThatCode(() -> refreshTokenService.isUsedJti(refreshTokenResponse.jti()))
                .doesNotThrowAnyException();
    }

    /**
     * 사용된 JWT인지 검증하는데 존재하지 않는 JTI라 false로 나오는 경우.
     * {@code isUsedJti}메소드는 {@code JtiValidator}내부에서 Jti가 유효한지 검증하는데 사용되는 메소드이다.
     * 이때 예를 들어 유저가 리프레쉬 토큰을 변조하여 username을 맞추고 jti가 틀렸을 경우 jti가 해킹당했다고 가정하고 username의 모든 리프레쉬 토큰을 무효화시킨다.
     *
     * @see JtiValidator
     * */
    @Test
    void isUsedJti_Fail_WhenJtiNotExist() {
        assertThat(refreshTokenService.isUsedJti("not_exist_jti")).isFalse();
    }

    /**
     * 토큰 무효화 성공 테스트.
     * */
    @Test
    void revokeToken_Success() {
        var refreshTokenResponse = refreshTokenService.create(createRequest());

        assertThatCode(() -> refreshTokenService.revokeToken(refreshTokenResponse.token()))
                .doesNotThrowAnyException();
    }

    /**
     * 일치하지 않는 jti이기 때문에 토큰 무효화에 실패하는 테스트.
     * */
    @Test
    void revokeToken_Fail_WhenJtiNotMatch() {
        refreshTokenService.create(createRequest());

        assertThatCode(() -> refreshTokenService.revokeToken("not_match_jti"))
                .isInstanceOf(NotFoundRefreshTokenException.class);
    }

    /**
     * Redis에서 Jti를 가져오는 테스트.
     * */
    @Test
    void getJtiFromRedis_Success() {
        refreshTokenService.create(createRequest());

        assertThatCode(() -> refreshTokenService.getJtiFromRedis(username))
                .doesNotThrowAnyException();
    }

    /**
     * Redis에서 Jti를 가져오는데 올바르지 않은 username이기에 실패하는 테스트.
     * */
    @Test
    void getJtiFromRedis_Fail_WhenUsernameNotMatch() {
        refreshTokenService.create(createRequest());

        assertThatCode(() -> refreshTokenService.getJtiFromRedis("not_match_username"))
                .isInstanceOf(FailedFindRefreshTokenException.class);
    }


    /**
     * 모든 토큰을 무효화하는데 성공하는 테스트.
     * */
    @Test
    void revokeAllToken_Success() {
        var token1 = refreshTokenService.create(createRequest());
        var token2 = refreshTokenService.create(createRequest());
        var token3 = refreshTokenService.create(createRequest());

        assertThatCode(() -> refreshTokenService.revokeAllToken(username))
                .doesNotThrowAnyException();

        assertThat(refreshTokenService.isUsedJti(token1.jti())).isTrue();
        assertThat(refreshTokenService.isUsedJti(token2.jti())).isTrue();
        assertThat(refreshTokenService.isUsedJti(token3.jti())).isTrue();
    }

    /**
     *  Jti 검증에 성공하는 테스트.
     * */
    @Test
    void validateJti_Success() {
        var refreshTokenResponse = refreshTokenService.create(createRequest());

        assertThatCode(() -> jtiValidator.validateJti(refreshTokenResponse.jti(), refreshTokenResponse.jti(), username))
                .doesNotThrowAnyException();
    }

    /**
     * 리프레쉬 토큰이 무효화되어 Jti 검증에 실패하는 테스트.
     * */
    @Test
    void validateJti_Fail_WhenTokenRevoked() {
        var refreshTokenResponse = refreshTokenService.create(createRequest());

        refreshTokenService.revokeToken(refreshTokenResponse.token());

        assertThatCode(() -> jtiValidator.validateJti(refreshTokenResponse.jti(), refreshTokenResponse.jti(), username))
                .isInstanceOf(RefreshTokenIsRevokedException.class);
    }

    /**
     * Jti가 일치하지 않아서 Jti 검증에 실패하는 테스트.
     * */
    @Test
    void validateJti_Fail_WhenJitNotMatch() {
        var refreshTokenResponse = refreshTokenService.create(createRequest());

        assertThatCode(() -> jtiValidator.validateJti(refreshTokenResponse.jti(), "not_match_jti", username))
                .isInstanceOf(UnMatchJTIException.class);
    }
}