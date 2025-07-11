package com.codingmate.redis;

import com.codingmate.ranking.dto.RankingReadDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RankingRedisRepository {
    private final ValueOperations<String, Object> valueOperations;
    private final RedisTemplate<String, Object> redisTemplate;

    public RankingRedisRepository(
            @Qualifier("objectValueOperations") ValueOperations<String, Object> valueOperations,
            @Qualifier("objectRedisTemplate") RedisTemplate<String, Object> redisTemplate
    ) {
        this.valueOperations = valueOperations;
        this.redisTemplate = redisTemplate;
    }

    public void save(String key, List<RankingReadDto> value) {
        valueOperations.set(key, value);
    }

    public List<RankingReadDto> getRanking(String key) {
        return (List<RankingReadDto>) valueOperations.get(key);
    }

    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }
}