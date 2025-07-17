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

    // RedisConfig에 두 개의 ValueOperation과 RedisTemplate가 있기에 @Qualifier를 사용해야 함.
    public RankingRedisRepository(
            @Qualifier("objectValueOperations") ValueOperations<String, Object> valueOperations,
            @Qualifier("objectRedisTemplate") RedisTemplate<String, Object> redisTemplate
    ) {
        this.valueOperations = valueOperations;
        this.redisTemplate = redisTemplate;
    }

    /**
     * Redis에 랭킹을 저장하기 위해서 사용하는 메소드.
     *
     * @param key   저장에 사용할 키 값 (ranking:daily:2025-01-01)
     * @param value 저장될 풀이 갯수 상위 10명에 대한 정보
     * */
    public void save(String key, List<SolveCountRankingDto> value) {
        valueOperations.set(key, value, Duration.ofDays(1));
    }

    /**
     * Redis에 저장된 랭킹을 가져오기 위한 메소드.
     *
     * <li>List형태를 가져오기 때문에 방어적 프로그래밍으로 내부 검증을 철저히 해야 함.</li>
     * <li>가져온 값이 null이거나 size가 0이면 예외를 발생시킴.</li>
     *
     * @exception ClassCastException            Redis에서 가져온 데이터 타입이 List가 아닐 때 발생하는 예외.
     * @exception IllegalStateException         Redis에서 가져온 데이터와 원하는 데이터 타입이 일치하지 않을 때 발생하는 예외.
     * @exception RankingIllegalTypeException   Redis에서 가져온 데이터가 빈 값이거나 List가 아닐 때 발생하는 예외.
     *
     * @param key 조회에 사용할 키 값 (ranking:daily:2025-01-01)
     * @return 오늘의 랭킹, 풀이 갯수 상위 10명에 대한 정보
     * */
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

    /**
     * Redis에 저장된 랭킹을 삭제하기 위한 메소드.
     *
     * @param key 삭제에 사용할 키 값 (ranking:daily:2025-01-01)
     * */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }
}