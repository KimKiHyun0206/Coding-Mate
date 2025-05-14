package com.codingMate.service.redis;

import com.codingMate.dto.response.token.TokenDto;
import com.codingMate.exception.dto.ErrorMessage;
import com.codingMate.exception.exception.redis.InvalidRefreshTokenException;
import com.codingMate.jwt.TokenProvider;
import com.codingMate.redis.RedisCacheInfo;
import com.codingMate.service.programmer.ProgrammerDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j
@Service
public class RefreshTokenService {
    private final RedisTemplate<String, RedisCacheInfo> redisTemplate;
    private final ValueOperations<String, RedisCacheInfo> valueOperations;
    private final TokenProvider tokenProvider;
    private final ProgrammerDetailsService programmerDetailsService;

    public RefreshTokenService(RedisTemplate<String, RedisCacheInfo> redisTemplate,
                               TokenProvider tokenProvider,
                               ProgrammerDetailsService programmerDetailsService
    ) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
        this.tokenProvider = tokenProvider;
        this.programmerDetailsService = programmerDetailsService;
    }

    public RedisCacheInfo saveToken(String token, String loginId, Collection<? extends GrantedAuthority> authorities) {
        LocalDateTime now = LocalDateTime.now();
        RedisCacheInfo info = RedisCacheInfo.builder()
                .authorities(authorities)
                .loginId(loginId)
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

    public TokenDto createAccessTokenFromRefreshToken(String refreshToken) {
        log.info("createAccessTokenFromRefreshToken({})", refreshToken);
        RedisCacheInfo redisCacheInfo = valueOperations.get(refreshToken);
        if (redisCacheInfo == null)
            throw new InvalidRefreshTokenException(ErrorMessage.INVALID_JWT, "요청한 refresh token이 유효하지 않습니다");

        //가져온 정보로 UserDetails 생성
        UserDetails userDetails = programmerDetailsService.loadUserByUsername(redisCacheInfo.getLoginId());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //UserDetails로 새로운 토큰 생성
        String newAccessToken = tokenProvider.createRefreshToken(authentication);
        String newRefreshToken = tokenProvider.createRefreshToken(authentication);

        //기존 토큰을 제거하고 새로운 refreshToken을 저장함 그리고
        Boolean delete = redisTemplate.delete(refreshToken);
        valueOperations.set(newRefreshToken, newRedisCacheInfo(redisCacheInfo));
        return new TokenDto(newAccessToken, newRefreshToken);
    }

    private RedisCacheInfo newRedisCacheInfo(RedisCacheInfo redisCacheInfo) {
        LocalDateTime now = LocalDateTime.now();
        return RedisCacheInfo.builder()
                .loginId(redisCacheInfo.getLoginId())
                .issuedAt(now)
                .expiresAt(now.plusMonths(3))
                .authorities(redisCacheInfo.getAuthorities())
                .build();
    }
}