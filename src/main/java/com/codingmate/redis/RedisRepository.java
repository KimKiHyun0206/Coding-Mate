package com.codingmate.redis;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RedisRepository {
    private final ValueOperations<String, RedisCacheInfo> valueOperations;
    private final RedisTemplate<String, RedisCacheInfo> redisTemplate;

    public void save(String token, RedisCacheInfo redisCacheInfo) {
        valueOperations.set(token, redisCacheInfo);
    }

    public Optional<RedisCacheInfo> getCacheInfo(String refreshToken) {
        return Optional.ofNullable(valueOperations.get(refreshToken));
    }

    public Boolean delete(String refreshToken) {
        return redisTemplate.delete(refreshToken);
    }
}