package com.codingmate.refreshtoken.service;

import com.codingmate.common.annotation.Explanation;
import com.codingmate.config.properties.JWTProperties;
import com.codingmate.exception.dto.ErrorMessage;
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
    private final RefreshTokenWriteRepository refreshTokenWriteRepository;

    private final String KEY_PREFIX;
    private final String KEY_SUFFIX;
    private final int MAX_TOKEN;
    private final int REDIS_TOKEN_EXPIRE_DAYS;

    protected RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            RedisRepository redisRepository,
            JWTProperties jwtProperties,
            RefreshTokenReadRepository refreshTokenReadRepository,
            RefreshTokenWriteRepository refreshTokenWriteRepository
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.redisRepository = redisRepository;
        this.REDIS_TOKEN_EXPIRE_DAYS = jwtProperties.refreshTokenExpirationDays();
        this.KEY_PREFIX = jwtProperties.redis().key().prefix();
        this.KEY_SUFFIX = jwtProperties.redis().key().suffix();
        this.MAX_TOKEN = jwtProperties.redis().maxToken();
        this.refreshTokenReadRepository = refreshTokenReadRepository;
        this.refreshTokenWriteRepository = refreshTokenWriteRepository;
    }

    /**
     * 리프레쉬 토큰을 생성합니다. 생성 전에 한 유저에게 발급된 리프레쉬 토큰이 몇 개인지 세어보고 만약 최대치를 넘겼을 경우 발급이 거부됩니다.
     * 저장은 DB와 Redis에 모두 저장됩니다.
     *
     * @param request 저장할 정보를 담은 dto
     * */
    @Transactional
    public RefreshTokenResponse create(RefreshTokenCreateRequest request) {
        log.debug("[RefreshTokenService] create({})", request);

        validateMaxTokenCount(request.userId());

        var entity = RefreshToken.toEntity(request, REDIS_TOKEN_EXPIRE_DAYS);

        log.debug("[RefreshTokenService] Try to save tokens: {}", entity.getToken().substring(20));

        var save = refreshTokenRepository.save(entity);
        saveInRedis(entity);

        return RefreshTokenResponse.of(save);
    }

    /**
     * 발급된 토큰이 몇 개인지 보고 최대치를 넘겼다면 예외를 발생시킵니다.
     *
     * @exception RefreshTokenOverMax 리프레쉬 토큰이 이미 최대값까지 발급되어있을 때 발생시키는 예외
     * */
    private void validateMaxTokenCount(Long programmerId){
        log.debug("[RefreshTokenService] Checking token count for user: {}", programmerId);
        Long count = refreshTokenReadRepository.countRefreshToken(programmerId);
        log.info("[RefreshTokenService] Token count: {}", count);
        if (count > MAX_TOKEN) {
            throw new RefreshTokenOverMax(
                    ErrorMessage.REFRESH_TOKEN_OVER_MAX,
                    String.format("최대 %d개의 토큰을 초과하여 발급이 불가능합니다.", MAX_TOKEN)
            );
        }
    }

    /**
     * jti가 이전에 사용되었는지 보기 위한 메소드.
     *
     * @param jti 검증할 jti
     * */
    @Transactional
    public boolean isUsedJti(String jti) {
        log.debug("[RefreshTokenService] isUsedJti({})", jti);
        return refreshTokenReadRepository.isUsedJti(jti);
    }

    /**
     * Redis에 정보를 저장하기 위한 메소드
     *
     * @param refreshToken 리프레쉬 토큰에서 정보를 가져와 key와 value를 만들어 저장합니다.
     * */
    private void saveInRedis(RefreshToken refreshToken) {
        log.debug("[RefreshTokenService] saveInRedis({})", refreshToken);

        String key = makeRedisKey(refreshToken.getUserId());
        String value = ScoreUtil.instantToScore(refreshToken.getIssuedAt()) + " " + refreshToken.getJti();
        log.info("[RefreshTokenService] Trying save token info to Redis: Key={}, Value={}", key, value);
        redisRepository.save(key, value);
    }

    /**
     * 토큰을 무효로 만들기 위한 메소드
     *
     * @param token 무효로 만들 토큰
     * */
    @Transactional
    public void revokeToken(String token) {
        String shortToken = shortToken(token);
        log.debug("[RefreshTokenService] makeRevoked({})", shortToken);

        refreshTokenRepository.findByToken(token).ifPresentOrElse(ref -> {
            log.info("[RefreshTokenService] Revoked refresh token: {}",shortToken);
            removeAndRevoke(ref);
        }, () -> log.warn("[RefreshTokenService] Can`t found refresh token in database: {}", shortToken));

        log.info("[RefreshTokenService] Refresh token make revoked true successfully: {}", shortToken);
    }

    /**
     * 토큰을 Redis에서 지우고 무효로 만들기 위한 메소드
     *
     * @param refreshToken 무효로 만들 리프레쉬 토큰
     * @implNote 위의 메소드가 번잡해지는 것을 막기 위해서 만든 메소드임
     * */
    private void removeAndRevoke(RefreshToken refreshToken){
        refreshToken.revoke();
        deleteFromRedis(refreshToken.getUserId());
    }

    /**
     * Redis에서 토큰 값을 지우기 위한 메소드
     *
     * @param id redis에서 삭제할 키 값
     * */
    private void deleteFromRedis(Long id) {
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

    /**
     * 한 유저가 발급한 모든 리프레쉬 토큰을 무효화하기 위한 메소드.
     *
     * @param programmerId 무효화할 토큰의 주인
     * */
    @Transactional
    public void revokeAllToken(Long programmerId){
        log.debug("[RefreshTokenService] revokeAllToken({})", programmerId);
        long l = refreshTokenWriteRepository.revokeAllToken(programmerId);
        log.info("[RefreshTokenService] {} tokens revoked successfully", l);
    }

    private String shortToken(String token) {
        return token.length() <= 20 ? token : token.substring(0, 20) + "...";
    }

    private String makeRedisKey(Long userId) {
        return KEY_PREFIX + userId + KEY_SUFFIX;
    }
}