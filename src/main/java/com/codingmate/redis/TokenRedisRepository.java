package com.codingmate.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Redis에 리프레쉬 토큰을 저장하고 읽어오는 레포지토리
 *
 * <li>기본적인 생성, 조회, 삭제 메소드만 존재한다</li>
 *
 * @author duskafka
 * */
@Slf4j
@Repository
public class TokenRedisRepository {
    private final ValueOperations<String, String> valueOperations;
    private final RedisTemplate<String, String> redisTemplate;

    public TokenRedisRepository(
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