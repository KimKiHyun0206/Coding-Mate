package com.codingmate.redis;

import com.codingmate.auth.dto.response.TokenResponse;
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

    /**
     * 주어진 리프레시 토큰을 사용하여 액세스 토큰과 리프레시 토큰을 갱신합니다.
     *
     * @param refreshToken 갱신할 기존 리프레시 토큰
     * @return 새로 발급된 액세스 토큰과 리프레시 토큰을 담은 TokenDto
     * @throws com.codingmate.exception.exception.redis.InvalidRefreshTokenException 리프레시 토큰이 유효하지 않거나 Redis에 없는 경우 발생
     * @throws com.codingmate.exception.exception.redis.FailedDeleteRefreshToken 기존 리프레시 토큰 삭제에 실패한 경우 발생
     */
    public TokenResponse refreshTokens(String refreshToken) {
        log.debug("[RedisTokenService] refreshTokens({})", refreshToken);
        log.info("[RedisTokenService] Refresh token renewal request for: {}", refreshToken);
        validateToken(refreshToken);    //토큰이 유효한지 검증한다

        var redisCacheInfo = getRefreshTokenDetail(refreshToken);
        log.debug("[RedisTokenService] Successfully retrieved refresh token details from Redis: ID={}, Authority={}", redisCacheInfo.id(), redisCacheInfo.authority());

        //가져온 정보로 토큰 생성
        String newAccessToken = tokenProvider
                .createAccessToken(
                        redisCacheInfo.id(),
                        redisCacheInfo.authority()
                );

        String newRefreshToken = tokenProvider
                .createRefreshToken(redisCacheInfo.id());
        log.debug("[RedisTokenService] New access token and refresh token generated.");

        //기존 토큰을 제거하고 새로운 refreshToken을 저장함
        renewToken(refreshToken, newRefreshToken, redisCacheInfo);
        log.info("[RedisTokenService] Tokens successfully refreshed. New Access Token Length: {}, New Refresh Token Length: {}", newAccessToken.length(), newRefreshToken.length());
        return TokenResponse.of(newAccessToken, newRefreshToken);
    }

    /**
     * 주어진 토큰, 사용자 ID, 권한 정보를 사용하여 리프레시 토큰을 Redis에 저장합니다.
     * 이 메서드는 주로 로그인 시점에 새로운 리프레시 토큰을 기록하는 데 사용됩니다.
     *
     * @param token 저장할 리프레시 토큰 문자열
     * @param id 토큰과 연관된 사용자 ID
     * @param authority 토큰과 연관된 사용자 권한
     */
    public void saveToken(String token, Long id, String authority) {
        log.info("[RedisTokenService] saveToken({}, {})", id, authority);   //토큰은 로깅하지 않음
        var now = LocalDateTime.now();
        var info = RefreshTokenDetail.of(
                id,
                authority,
                now,
                now.plusDays(REDIS_TOKEN_EXPIRE_DAYS) // 설정된 만료일만큼 추가
        );
        redisRepository.save(token, info);
        log.debug("[RedisTokenService] Refresh token saved. Token length: {}, ID: {}, Authority: {}, Expires on: {}", token.length(), id, authority, info.expiresAt());
    }

    /**
     * 주어진 리프레시 토큰의 유효성을 검증합니다.
     * 유효성 검증은 {@code TokenProvider}를 통해 수행됩니다.
     *
     * @param refreshToken 유효성을 검증할 리프레시 토큰
     */
    private void validateToken(String refreshToken) {
        log.debug("[RedisTokenService] validateToken({})", refreshToken);
        tokenProvider.validateToken(refreshToken);
        log.debug("[RedisTokenService] Refresh token valid.");
    }

    /**
     * Redis에서 주어진 리프레시 토큰에 해당하는 상세 정보를 조회합니다.
     * 만약 토큰에 대한 정보가 Redis에 없으면 {@code InvalidRefreshTokenException}을 발생시킵니다.
     *
     * @param refreshToken 상세 정보를 조회할 리프re시 토큰
     * @return 조회된 RefreshTokenDetail 객체
     * @throws InvalidRefreshTokenException Redis에 해당 리프레시 토큰 정보가 없는 경우 발생
     */
    private RefreshTokenDetail getRefreshTokenDetail(String refreshToken) {
        log.debug("[RedisTokenService] getRefreshTokenDetail({})", refreshToken);
        return redisRepository.getRefreshTokenDetail(refreshToken)
                .orElseThrow(() -> {
                    log.warn("[RedisTokenService] Failed to retrieve refresh token details. Token not found in Redis: {}", refreshToken);
                    return new InvalidRefreshTokenException(
                            ErrorMessage.INVALID_JWT,
                            "The requested refresh token is invalid or expired in Redis.");
                });
    }

    /**
     * 기존 리프레시 토큰을 삭제하고 새로운 리프레시 토큰을 Redis에 저장하여 갱신합니다.
     * 이 메서드는 토큰 갱신 흐름의 일부로 사용됩니다.
     *
     * @param oldRefreshToken 삭제할 기존 리프레시 토큰
     * @param newRefreshToken 새로 저장할 리프레시 토큰
     * @param oldRefreshTokenDetail 기존 리프레시 토큰의 상세 정보 (새로운 토큰 생성에 사용)
     */
    private void renewToken(String oldRefreshToken, String newRefreshToken, RefreshTokenDetail oldRefreshTokenDetail) {
        log.debug("[RedisTokenService] renewToken({}, {})", oldRefreshToken.substring(0, 20), newRefreshToken.substring(0, 20));    //20자만 로깅함
        // 기존 토큰 제거
        deleteRefreshToken(oldRefreshToken);
        log.debug("[RedisTokenService] Old refresh token deleted from Redis.");
        // 새로운 토큰 저장
        redisRepository.save(
                newRefreshToken,
                createNewRefreshTokenDetail(oldRefreshTokenDetail)
        );
        log.debug("[RedisTokenService] New refresh token saved to Redis.");
    }

    /**
     * 새로운 리프레시 토큰을 위한 상세 정보를 생성합니다.
     * 기존 토큰의 사용자 ID와 권한을 재사용하고, 현재 시간을 기준으로 새로운 만료 시간을 설정합니다.
     *
     * @param refreshTokenDetail 기존 리프레시 토큰의 상세 정보
     * @return 새로 생성된 RefreshTokenDetail 객체
     */
    private RefreshTokenDetail createNewRefreshTokenDetail(RefreshTokenDetail refreshTokenDetail) {
        log.debug("[RedisTokenService] createNewRefreshTokenDetail({})", refreshTokenDetail);

        var now = LocalDateTime.now();
        log.debug("[RedisTokenService] Creating new refresh token details with ID: {}, Authority: {}, and expiration for {} days.",
                refreshTokenDetail.id(), refreshTokenDetail.authority(), REDIS_TOKEN_EXPIRE_DAYS);

        return RefreshTokenDetail.of(
                refreshTokenDetail.id(),
                refreshTokenDetail.authority(),
                now,
                now.plusDays(REDIS_TOKEN_EXPIRE_DAYS)
        );
    }

    /**
     * 주어진 리프레시 토큰을 Redis에서 삭제합니다.
     * 삭제 작업이 실패할 경우 {@code FailedDeleteRefreshToken} 예외를 발생시킵니다.
     *
     * @param token 삭제할 리프레시 토큰 문자열
     * @throws FailedDeleteRefreshToken 리프레시 토큰 삭제에 실패한 경우 발생
     */
    public void deleteRefreshToken(String token) {
        log.debug("[RedisTokenService] deleteRefreshToken({})", token.substring(0, 20));
        if (!redisRepository.delete(token)) {
            log.error("[RedisTokenService] Failed to delete refresh token from Redis: {}", token);
            throw new FailedDeleteRefreshToken(
                    ErrorMessage.FAILED_DELETE_REFRESH_TOKEN,
                    "Failed to delete the requested refresh token."
            );
        }
        log.info("[RedisTokenService] Refresh token deleted successfully: {}", token);
    }
}