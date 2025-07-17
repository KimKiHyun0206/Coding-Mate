package com.codingmate.refreshtoken.service;

import com.codingmate.config.properties.JWTProperties;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.jwt.NotFoundRefreshTokenException;
import com.codingmate.exception.exception.jwt.RefreshTokenOverMaxException;
import com.codingmate.exception.exception.redis.FailedFindRefreshTokenException;
import com.codingmate.redis.TokenRedisRepository;
import com.codingmate.refreshtoken.domain.RefreshToken;
import com.codingmate.refreshtoken.dto.request.RefreshTokenCreateRequest;
import com.codingmate.refreshtoken.dto.response.RefreshTokenResponse;
import com.codingmate.refreshtoken.repository.RefreshTokenReadRepository;
import com.codingmate.refreshtoken.repository.RefreshTokenRepository;
import com.codingmate.refreshtoken.repository.RefreshTokenWriteRepository;
import com.codingmate.util.InstantUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 리프레쉬 토큰 관리를 위한 CRUD 서비스
 *
 * @author duskafka
 */
@Slf4j
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenRedisRepository tokenRedisRepository;
    private final RefreshTokenReadRepository refreshTokenReadRepository;
    private final RefreshTokenWriteRepository refreshTokenWriteRepository;

    private final String KEY_PREFIX;
    private final String KEY_SUFFIX;
    private final int MAX_TOKEN;
    private final int REDIS_TOKEN_EXPIRE_DAYS;

    protected RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            TokenRedisRepository tokenRedisRepository,
            JWTProperties jwtProperties,
            RefreshTokenReadRepository refreshTokenReadRepository,
            RefreshTokenWriteRepository refreshTokenWriteRepository
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenRedisRepository = tokenRedisRepository;
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
     */
    @Transactional
    public RefreshTokenResponse create(RefreshTokenCreateRequest request) {
        log.debug("[RefreshTokenService] create({})", request);

        validateMaxTokenCount(request.username());

        var entity = RefreshToken.toEntity(request, REDIS_TOKEN_EXPIRE_DAYS);
        var save = refreshTokenRepository.save(entity);
        saveInRedis(entity);

        log.info("[RefreshTokenService] 리프레쉬 토큰이 데이터베이스와 Redis에 저장되었습니다: id={}", save.getId());
        return RefreshTokenResponse.of(save);
    }

    /**
     * 발급된 토큰이 몇 개인지 보고 최대치를 넘겼다면 예외를 발생시킵니다.
     *
     * @throws RefreshTokenOverMaxException 리프레쉬 토큰이 이미 최대값까지 발급되어있을 때 발생시키는 예외
     */
    private void validateMaxTokenCount(String programmerId) {
        log.debug("[RefreshTokenService] validateMaxTokenCount({})", programmerId);
        if (refreshTokenReadRepository.countRefreshToken(programmerId) > MAX_TOKEN) {
            throw new RefreshTokenOverMaxException(
                    ErrorMessage.REFRESH_TOKEN_OVER_MAX,
                    String.format("최대 %d개의 토큰을 초과하여 발급이 불가능합니다.", MAX_TOKEN)
            );
        }
    }

    /**
     * jti가 이전에 사용되었는지 보기 위한 메소드.
     *
     * @param jti 검증할 jti
     */
    @Transactional
    public boolean isUsedJti(String jti) {
        log.debug("[RefreshTokenService] isUsedJti({})", jti);
        return refreshTokenReadRepository.isUsedJti(jti);
    }

    /**
     * Redis에 정보를 저장하기 위한 메소드
     *
     * @param refreshToken 리프레쉬 토큰에서 정보를 가져와 key와 value를 만들어 저장합니다.
     */
    private void saveInRedis(RefreshToken refreshToken) {
        log.debug("[RefreshTokenService] saveInRedis({})", refreshToken);

        String key = makeRedisKey(refreshToken.getUsername());
        String value = InstantUtil.instantToScore(refreshToken.getIssuedAt()) + " " + refreshToken.getJti();

        tokenRedisRepository.save(key, value);

        log.info("[RefreshTokenService] 리프레쉬 토큰을 Redis에 저장하였습니다: key={}, value={}", key, value);
    }

    /**
     * 토큰을 무효로 만들기 위한 메소드
     *
     * @param token 무효로 만들 토큰
     */
    @Transactional
    public void revokeToken(String token) {
        String shortToken = shortToken(token);
        log.debug("[RefreshTokenService] revokeToken({})", shortToken);

        RefreshToken ref = refreshTokenRepository.findByToken(token).orElseThrow(() ->
                new NotFoundRefreshTokenException(
                        ErrorMessage.NOT_FOUND_REFRESH_TOKEN,
                        "리프레쉬 토큰을 찾지 못했습니다."
                )
        );

        removeAndRevoke(ref);
        log.info("[RefreshTokenService] 토큰 무효화에 성공했습니다.");
    }

    /**
     * 토큰을 Redis에서 지우고 무효로 만들기 위한 메소드
     *
     * @param refreshToken 무효로 만들 리프레쉬 토큰
     * @implNote 위의 메소드가 번잡해지는 것을 막기 위해서 만든 메소드임
     */
    private void removeAndRevoke(RefreshToken refreshToken) {
        refreshToken.revoke();
        deleteFromRedis(refreshToken.getUsername());
    }

    /**
     * Redis에서 토큰 값을 지우기 위한 메소드
     *
     * @param username redis에서 삭제할 키 값
     */
    private void deleteFromRedis(String username) {
        log.debug("[RefreshTokenService] deleteRefreshTokenInRedis({})", username);
        boolean b = tokenRedisRepository.delete(makeRedisKey(username));
        log.info("[RefreshTokenService] Refresh token deleted successfully: {}", b);
    }

    /**
     * @param username key를 조합하는데 필요한 값
     * @return Redis에 저장된 jti
     * @implNote makeRedisKey 메소드를 사용해서 키 형태로 만든다.
     */
    public String getJtiFromRedis(String username) {
        log.debug("[RedisTokenService] getJtiFromRedis({})", username);
        return tokenRedisRepository.getValue(makeRedisKey(username)).orElseThrow(
                () -> new FailedFindRefreshTokenException(
                        ErrorMessage.FAILED_DELETE_REFRESH_TOKEN,
                        "요청한 토큰으로 Redis에서 토큰을 찾지 못했습니다"
                )
        ).split(" ")[1];    // 1749124821607 6c6dd655-cff3-4272-a8cf-848614ba152c 이러한 형태를 가질 때 띄어쓰기를 하고 뒤의 것이 jti
    }

    /**
     * 한 유저가 발급한 모든 리프레쉬 토큰을 무효화하기 위한 메소드.
     *
     * @param username 무효화할 토큰의 주인
     */
    @Transactional
    public void revokeAllToken(String username) {
        log.debug("[RefreshTokenService] revokeAllToken({})", username);
        long l = refreshTokenWriteRepository.revokeAllToken(username);
        log.info("[RefreshTokenService] {} tokens revoked successfully", l);
    }

    private String shortToken(String token) {
        return token.length() <= 20 ? token : token.substring(0, 20) + "...";
    }

    private String makeRedisKey(String username) {
        return KEY_PREFIX + username + KEY_SUFFIX;
    }
}