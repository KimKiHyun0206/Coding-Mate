package com.codingmate.redis;

import com.codingmate.common.annotation.Explanation;
import com.codingmate.ranking.dto.SolveCountRankingDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

@Repository
@Explanation(
        responsibility = "Redis에 저장된 Ranking 조작",
        detail = "Redis에 Ranking을 저장하는데 이를 조회하고 저장하는 역할을 한다.",
        lastReviewed = "2025.07.13"
)
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

    public void save(String key, List<SolveCountRankingDto> value) {
        valueOperations.set(key, value, Duration.ofDays(1));
    }

    public List<SolveCountRankingDto> getRanking(String key) {
        Object value = valueOperations.get(key);
        if (value == null) {
            return List.of();
        }
        try {
            return (List<SolveCountRankingDto>) value;
        } catch (ClassCastException e) {
            throw new IllegalStateException("Redis 데이터 타입 불일치", e);
        }
    }

    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }
}