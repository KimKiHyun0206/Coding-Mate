package com.codingmate.redis;

import com.codingmate.common.annotation.Explanation;
import com.codingmate.ranking.dto.RankingReadDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

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