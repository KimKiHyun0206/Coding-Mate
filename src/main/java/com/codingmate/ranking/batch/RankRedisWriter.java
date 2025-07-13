package com.codingmate.ranking.batch;

import com.codingmate.common.annotation.Explanation;
import com.codingmate.config.properties.RankingProperties;
import com.codingmate.ranking.dto.RankingReadDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

@Slf4j
@Component
@RequiredArgsConstructor
@Explanation(
        responsibility = "RankReader가 반환한 List를 Redis에 저장한다.",
        detail = "프로퍼티에서 얻은 키로 Redis에 데이터를 저장한다.",
        lastReviewed = "2025.07.13"
)
public class RankRedisWriter implements ItemWriter<RankingReadDto>, StepExecutionListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RankingProperties rankingProperties;
    private PriorityQueue<RankingReadDto> top10;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        top10 = new PriorityQueue<>(Comparator.comparingLong(RankingReadDto::score)); // 오름차순
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        String key = rankingProperties.getKey(); // 예: ranking:daily:2025-07-11
        List<RankingReadDto> sorted = new ArrayList<>(top10);
        sorted.sort(Comparator.comparingLong(RankingReadDto::score).reversed());
        sorted.forEach(s -> {
            log.info("WRITER {} ", s.toString());
        });

        redisTemplate.opsForValue().set(key, sorted);
        redisTemplate.expire(key, Duration.ofDays(1));

        log.info("Redis 저장 key={} value={}", key, sorted);

        return ExitStatus.COMPLETED;
    }

    @Override
    public void write(Chunk<? extends RankingReadDto> chunk) throws Exception {
        for (RankingReadDto user : chunk.getItems()) {
            top10.offer(user);
            if (top10.size() > 10) {
                top10.poll(); // 가장 낮은 점수 제거
            }
        }
    }
}