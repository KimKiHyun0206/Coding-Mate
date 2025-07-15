package com.codingmate.redis;

import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.ranking.RankingIllegalTypeException;
import com.codingmate.ranking.dto.SolveCountRankingDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

/**
 * Redis에 저장된 Ranking을 조작하는 레포지토리
 *
 * <li>Redis에 Ranking을 저장하는데 이를 조회하고 저장하는 역할을 한다.</li>
 * <li>기본적인 생성, 조회, 삭제 메소드만 존재한다</li>
 *
 * @author duskafka
 * */
@Slf4j
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

    public void save(String key, List<SolveCountRankingDto> value) {
        valueOperations.set(key, value, Duration.ofDays(1));
    }

    public List<SolveCountRankingDto> getRanking(String key) {
        Object value = valueOperations.get(key);
        if (value == null) {
            return List.of();
        }
        try {
            if(!(value instanceof List<?>)) {
                throw new RankingIllegalTypeException(ErrorMessage.RANKING_ILLEGAL_TYPE, "Redis 데이터가 List 타입이 아닙니다.");
            }
            List<?> rawList = (List<?>) value;
            if(!rawList.isEmpty() && !(rawList.get(0) instanceof SolveCountRankingDto)) {
                throw new RankingIllegalTypeException(ErrorMessage.RANKING_ILLEGAL_TYPE, "Redis 데이터가 예상한 데이터 타입(SolveCountRankingDto)이 아닙니다.");
            }

            return (List<SolveCountRankingDto>) value;
        } catch (ClassCastException e) {
            log.error("Redis에서 랭킹 데이터 타입 캐스팅 실패, key: {}, value type: {}", key, value.getClass());
            throw new IllegalStateException("Redis 데이터 타입 불일치: " + value.getClass(), e);
        }
    }

    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }
}