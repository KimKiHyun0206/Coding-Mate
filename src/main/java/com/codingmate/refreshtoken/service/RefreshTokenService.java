package com.codingmate.refreshtoken.service;

import com.codingmate.common.annotation.Explanation;
import com.codingmate.config.properties.JWTProperties;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.jwt.RefreshTokenIsRevoked;
import com.codingmate.exception.exception.jwt.RefreshTokenOverMax;
import com.codingmate.exception.exception.redis.FailedFindRefreshToken;
import com.codingmate.redis.RedisRepository;
import com.codingmate.refreshtoken.domain.RefreshToken;
import com.codingmate.refreshtoken.dto.request.RefreshTokenCreateRequest;
import com.codingmate.refreshtoken.dto.response.RefreshTokenResponse;
import com.codingmate.refreshtoken.repository.RefreshTokenReadRepository;
import com.codingmate.refreshtoken.repository.RefreshTokenRepository;
import com.codingmate.refreshtoken.repository.RefreshTokenWriteRepository;
import com.codingmate.util.ScoreUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Explanation(
        responsibility = "리프레쉬 토큰 관리(CRUD)",
        domain = "RefreshToken",
        lastReviewed = "2025.06.05"
)
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisRepository redisRepository;
    private final RefreshTokenReadRepository refreshTokenReadRepository;

    private final int REDIS_TOKEN_EXPIRE_DAYS;
    private final String KEY_PREFIX;
    private final String KEY_SUFFIX;
    private final int MAX_TOKEN;
    private final RefreshTokenWriteRepository refreshTokenWriteRepository;

    protected RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            RedisRepository redisRepository,
            JWTProperties jwtProperties,
            RefreshTokenReadRepository refreshTokenReadRepository,
            RefreshTokenWriteRepository refreshTokenWriteRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.redisRepository = redisRepository;
        this.REDIS_TOKEN_EXPIRE_DAYS = jwtProperties.expirationDays();
        this.KEY_PREFIX = jwtProperties.redis().key().prefix();
        this.KEY_SUFFIX = jwtProperties.redis().key().suffix();
        this.MAX_TOKEN = jwtProperties.redis().maxToken();
        this.refreshTokenReadRepository = refreshTokenReadRepository;
        this.refreshTokenWriteRepository = refreshTokenWriteRepository;
    }


    @Transactional
    public RefreshTokenResponse create(RefreshTokenCreateRequest request) {
        log.debug("[RefreshTokenService] create({})", request);

        log.debug("[RefreshTokenService] Check Programmer`s refresh token");
        Long count = refreshTokenReadRepository.countRefreshToken(request.userId());
        log.info("[RefreshTokenService] Programmer`s refresh token count: {}", count);
        if (count > MAX_TOKEN) {    //만약 MAX_TOKEN의 수보다 많을 시 토큰 발급을 거부함
            throw new RefreshTokenOverMax(
                    ErrorMessage.REFRESH_TOKEN_OVER_MAX,
                    String.format("최대치 %d개가 넘게 발급되어 더이상 리프레시 토큰 발급이 불가능합니다.", MAX_TOKEN)
            );
        }

        var entity = RefreshToken.toEntity(request, REDIS_TOKEN_EXPIRE_DAYS);

        log.debug("[RefreshTokenService] Try to save tokens: {}", entity.getToken().substring(20));

        RefreshToken save = refreshTokenRepository.save(entity);
        saveInRedis(entity);
        return RefreshTokenResponse.of(save);
    }

    @Transactional
    public boolean isUsedJti(String jti) {
        log.debug("[RefreshTokenService] isUsedJti({})", jti);
        return refreshTokenReadRepository.isUsedJti(jti);
    }

    private void saveInRedis(RefreshToken refreshToken) {
        log.debug("[RefreshTokenService] saveInRedis({})", refreshToken);

        String key = makeRedisKey(refreshToken.getUserId());
        String value = ScoreUtil.instantToScore(refreshToken.getIssuedAt()) + " " + refreshToken.getJti();
        log.info("[RefreshTokenService] Trying save token info to Redis: Key={}, Value={}", key, value);
        redisRepository.save(key, value);
    }

    public String makeRedisKey(Long userId) {
        return KEY_PREFIX + userId + KEY_SUFFIX;
    }

    @Transactional
    public void makeIsRevokedTrue(String token) {
        log.debug("[RefreshTokenService] makeRevoted({})", token.substring(0, 20));
        refreshTokenRepository.findByToken(token).ifPresentOrElse(ref -> {
            log.info("[RefreshTokenService] Revoked refresh token: {}", token.substring(20));
            ref.revoke();
            deleteRefreshTokenInRedis(ref.getId());
        }, () -> {
            log.warn("[RefreshTokenService] Can`t found refresh token in database: {}", token.substring(20));
        });
        log.info("[RefreshTokenService] Refresh token make revoked true successfully: {}", token);
    }

    private void deleteRefreshTokenInRedis(Long id) {
        log.debug("[RefreshTokenService] deleteRefreshTokenInRedis({})", id);
        boolean b = redisRepository.delete(makeRedisKey(id));
        log.info("[RefreshTokenService] Refresh token deleted successfully: {}", b);
    }

    /**
     * @param id key를 조합하는데 필요한 id
     * @implNote makeRedisKey 메소드를 사용해서 키 형태로 만든다.
     * @return Redis에 저장된 jti
     * */
    public String getJtiFromRedis(Long id) {
        log.debug("[RedisTokenService] getJtiFromRedis({})", id);
        return redisRepository.getValue(makeRedisKey(id)).orElseThrow(
                () -> {
                    log.warn("[RedisTokenService] Failed to find a refresh token: {}", id);
                    return new FailedFindRefreshToken(
                            ErrorMessage.FAILED_DELETE_REFRESH_TOKEN,
                            "요청한 토큰으로 Redis에서 토큰을 찾지 못했습니다"
                    );
                }
        ).split(" ")[1];    // 1749124821607 6c6dd655-cff3-4272-a8cf-848614ba152c 이러한 형태를 가질 때 띄어쓰기를 하고 뒤의 것이 jti
    }

    @Transactional
    public void revokeAllToken(Long programmerId){
        log.debug("[RefreshTokenService] revokeAllToken({})", programmerId);
        long l = refreshTokenWriteRepository.revokeAllToken(programmerId);
        log.info("[RefreshTokenService] {} tokens revoked successfully", l);
    }
}