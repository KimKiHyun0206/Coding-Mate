package com.codingMate.service.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RefreshTokenService {
    private final RedisTemplate<String, Long> redisTemplate;
    private final ValueOperations<String, Long> valueOperations;

    public RefreshTokenService(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    public Long save(String token, Long userLoginId) {
        log.info("save({}, {})", token, userLoginId);
        valueOperations.set(token, userLoginId);

        return userLoginId;
    }

    public Long find(String token) {
        log.info("find({})", token);
        return valueOperations.get(token);
    }
}