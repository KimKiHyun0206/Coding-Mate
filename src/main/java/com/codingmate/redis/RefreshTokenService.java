package com.codingmate.redis;

import com.codingmate.auth.dto.response.TokenDto;
import com.codingmate.config.properties.JWTProperties;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.redis.FailedDeleteRefreshToken;
import com.codingmate.exception.exception.redis.InvalidRefreshTokenException;
import com.codingmate.exception.exception.redis.RefreshTokenIsNullException;
import com.codingmate.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
public class RefreshTokenService {
    private final TokenProvider tokenProvider;
    private final RedisRepository redisRepository;
    private final int REDIS_TOKEN_EXPIRE_DAYS;

    public RefreshTokenService(
            TokenProvider tokenProvider,
            RedisRepository redisRepository,
            JWTProperties jwtProperties
    ) {
        this.tokenProvider = tokenProvider;
        this.redisRepository = redisRepository;
        this.REDIS_TOKEN_EXPIRE_DAYS = jwtProperties.getExpirationDays();
    }

    public TokenDto refreshTokens(String refreshToken) {
        validateToken(refreshToken);    //토큰이 유효한지 검증한다

        var redisCacheInfo = getCacheInfo(refreshToken);

        //가져온 정보로 토큰 생성
        String newAccessToken = tokenProvider
                .createAccessToken(
                        redisCacheInfo.id(),
                        redisCacheInfo.authority()
                );

        String newRefreshToken = tokenProvider
                .createRefreshToken(redisCacheInfo.id());

        //기존 토큰을 제거하고 새로운 refreshToken을 저장함
        renewToken(refreshToken, newRefreshToken, redisCacheInfo);
        return TokenDto.of(newAccessToken, newRefreshToken);
    }

    public void saveToken(String token, Long id, String authority) {
        var now = LocalDateTime.now();
        var info = RedisCacheInfo.of(
                id,
                authority,
                now,
                now.plusDays(REDIS_TOKEN_EXPIRE_DAYS)
        );
        redisRepository.save(token, info);
    }

    private void validateToken(String refreshToken) {
        Objects.requireNonNull(refreshToken, () -> {
            throw new RefreshTokenIsNullException(
                    ErrorMessage.REFRESH_TOKEN_IS_NULL,
                    "갱신할 리프레시 토큰이 없습니다"
            );
        });
    }

    private RedisCacheInfo getCacheInfo(String refreshToken) {
        return redisRepository.getCacheInfo(refreshToken)
                .orElseThrow(() ->
                        new InvalidRefreshTokenException(
                                ErrorMessage.INVALID_JWT,
                                "요청된 리프레시 토큰이 유효하지 않습니다")
                );
    }

    private void renewToken(String oldRefreshToken, String newRefreshToken, RedisCacheInfo oldRedisCacheInfo) {
        // 기존 토큰 제거
        deleteRefreshToken(oldRefreshToken);
        // 새로운 토큰 저장
        redisRepository.save(
                newRefreshToken,
                createNewRedisCacheInfo(oldRedisCacheInfo)
        );
    }

    private RedisCacheInfo createNewRedisCacheInfo(RedisCacheInfo redisCacheInfo) {
        var now = LocalDateTime.now();
        return RedisCacheInfo.of(
                redisCacheInfo.id(),
                redisCacheInfo.authority(),
                now,
                now.plusDays(REDIS_TOKEN_EXPIRE_DAYS)
        );
    }

    public void deleteRefreshToken(String token) {
        if (!redisRepository.delete(token)) {
            throw new FailedDeleteRefreshToken(
                    ErrorMessage.FAILED_DELETE_REFRESH_TOKEN,
                    "요청한 리프레시 토큰을 지우는데 실패했습니다"
            );
        }
    }
}