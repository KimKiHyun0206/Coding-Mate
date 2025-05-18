package com.codingMate.service.redis;

import com.codingMate.dto.response.token.TokenDto;
import com.codingMate.exception.dto.ErrorMessage;
import com.codingMate.exception.exception.redis.InvalidRefreshTokenException;
import com.codingMate.jwt.TokenProvider;
import com.codingMate.redis.RedisCacheInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class RefreshTokenService {
    private final RedisTemplate<String, RedisCacheInfo> redisTemplate;
    private final ValueOperations<String, RedisCacheInfo> valueOperations;
    private final TokenProvider tokenProvider;

    public RefreshTokenService(RedisTemplate<String, RedisCacheInfo> redisTemplate,
                               TokenProvider tokenProvider
    ) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
        this.tokenProvider = tokenProvider;
    }

    public RedisCacheInfo saveToken(String token, Long id, String authority) {
        LocalDateTime now = LocalDateTime.now();
        RedisCacheInfo info = RedisCacheInfo.builder()
                .authority(authority)
                .id(id)
                .issuedAt(now)
                .expiresAt(now.plusMonths(3))
                .build();
        log.info("save({}, {})", token, info);
        valueOperations.set(token, info);

        return info;
    }

    public boolean deleteRefreshToken(String token) {
        return Boolean.TRUE.equals(redisTemplate.delete(token));
    }

    public TokenDto createAccessTokenFromRefreshToken(String refreshToken) throws InvalidRefreshTokenException {
        log.info("createAccessTokenFromRefreshToken({})", refreshToken);
        RedisCacheInfo redisCacheInfo = valueOperations.get(refreshToken);
        if (redisCacheInfo == null)
            throw new InvalidRefreshTokenException(ErrorMessage.INVALID_JWT, "요청한 refresh token이 유효하지 않습니다");

        //가져온 정보로 UserDetails 생성
        Long id = redisCacheInfo.getId();
        String role = redisCacheInfo.getAuthority();
        String newAccessToken = tokenProvider.createAccessToken(id, role);
        String newRefreshToken = tokenProvider.createRefreshToken(id, role);
        log.info("new Tokens \n {} \n {}", newAccessToken, newRefreshToken);

        //기존 토큰을 제거하고 새로운 refreshToken을 저장함 그리고
        Boolean delete = redisTemplate.delete(refreshToken);
        valueOperations.set(newRefreshToken, newRedisCacheInfo(redisCacheInfo));
        return new TokenDto(newAccessToken, newRefreshToken);
    }

    private RedisCacheInfo newRedisCacheInfo(RedisCacheInfo redisCacheInfo) {
        LocalDateTime now = LocalDateTime.now();
        return RedisCacheInfo.builder()
                .id(redisCacheInfo.getId())
                .issuedAt(now)
                .expiresAt(now.plusMonths(3))
                .authority(redisCacheInfo.getAuthority())
                .build();
    }
}