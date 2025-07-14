package com.codingmate.redis;

import com.codingmate.common.annotation.Explanation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@Explanation(
        responsibility = "Redis 조작",
        detail = "기본적인 생성, 조회, 삭제 메소드만 존재함",
        lastReviewed = "2025.06.05"
)
public class TokenRedisRepository {
    private final ValueOperations<String, String> valueOperations;
    private final RedisTemplate<String, String> redisTemplate;

    protected TokenRedisRepository(
            @Qualifier("stringValueOperations") ValueOperations<String, String> valueOperations,
            @Qualifier("myStringRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.valueOperations = valueOperations;
        this.redisTemplate = redisTemplate;
    }

    public void save(String key, String value) {
        valueOperations.set(key, value);
    }

    public Optional<String> getValue(String refreshToken) {
        return Optional.ofNullable(valueOperations.get(refreshToken));
    }

    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }
}