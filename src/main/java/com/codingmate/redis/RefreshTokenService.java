package com.codingmate.redis;

import com.codingmate.auth.dto.response.TokenDto;
import com.codingmate.config.properties.JWTProperties;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.redis.InvalidRefreshTokenException;
import com.codingmate.exception.exception.redis.RefreshTokenIsNullException;
import com.codingmate.jwt.TokenProvider;
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
    private final int expirationDays;

    public RefreshTokenService(RedisTemplate<String, RedisCacheInfo> redisTemplate,
                               TokenProvider tokenProvider,
                               JWTProperties jwtProperties
    ) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
        this.tokenProvider = tokenProvider;
        this.expirationDays = jwtProperties.getExpirationDays();
    }

    public void saveToken(String token, Long id, String authority) {
        LocalDateTime now = LocalDateTime.now();
        RedisCacheInfo info = RedisCacheInfo.builder()
                .authority(authority)
                .id(id)
                .issuedAt(now)
                .expiresAt(now.plusDays(expirationDays))
                .build();
        log.info("save({}, {})", token, info);
        valueOperations.set(token, info);
    }

    public void deleteRefreshToken(String token) {
        redisTemplate.delete(token);
    }

    public TokenDto createAccessTokenFromRefreshToken(String refreshToken){
        if(refreshToken == null) throw new RefreshTokenIsNullException(ErrorMessage.REFRESH_TOKEN_IS_NULL,"갱신할  Refresh Token이 없습니다");
        RedisCacheInfo redisCacheInfo = valueOperations.get(refreshToken);
        if (redisCacheInfo == null)
            throw new InvalidRefreshTokenException(ErrorMessage.INVALID_JWT, "요청한 refresh token이 유효하지 않습니다");

        //가져온 정보로 UserDetails 생성
        Long id = redisCacheInfo.getId();
        String role = redisCacheInfo.getAuthority();
        String newAccessToken = tokenProvider.createAccessToken(id, role);
        String newRefreshToken = tokenProvider.createRefreshToken(id);

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