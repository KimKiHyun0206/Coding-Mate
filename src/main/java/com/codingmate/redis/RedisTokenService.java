package com.codingmate.redis;

import com.codingmate.auth.dto.response.TokenDto;
import com.codingmate.config.properties.JWTProperties;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.redis.FailedDeleteRefreshToken;
import com.codingmate.exception.exception.redis.InvalidRefreshTokenException;
import com.codingmate.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class RedisTokenService {
    private final TokenProvider tokenProvider;
    private final RedisRepository redisRepository;
    private final int REDIS_TOKEN_EXPIRE_DAYS;

    public RedisTokenService(
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

        var redisCacheInfo = getRefreshTokenDetail(refreshToken);

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
        var info = RefreshTokenDetail.of(
                id,
                authority,
                now,
                now.plusDays(REDIS_TOKEN_EXPIRE_DAYS)
        );
        redisRepository.save(token, info);
    }

    private void validateToken(String refreshToken) {
        tokenProvider.validateToken(refreshToken);
    }

    private RefreshTokenDetail getRefreshTokenDetail(String refreshToken) {
        return redisRepository.getRefreshTokenDetail(refreshToken)
                .orElseThrow(() ->
                        new InvalidRefreshTokenException(
                                ErrorMessage.INVALID_JWT,
                                "요청된 리프레시 토큰이 유효하지 않습니다")
                );
    }

    private void renewToken(String oldRefreshToken, String newRefreshToken, RefreshTokenDetail oldRefreshTokenDetail) {
        // 기존 토큰 제거
        deleteRefreshToken(oldRefreshToken);
        // 새로운 토큰 저장
        redisRepository.save(
                newRefreshToken,
                createNewRefreshTokenDetail(oldRefreshTokenDetail)
        );
    }

    private RefreshTokenDetail createNewRefreshTokenDetail(RefreshTokenDetail refreshTokenDetail) {
        var now = LocalDateTime.now();
        return RefreshTokenDetail.of(
                refreshTokenDetail.id(),
                refreshTokenDetail.authority(),
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