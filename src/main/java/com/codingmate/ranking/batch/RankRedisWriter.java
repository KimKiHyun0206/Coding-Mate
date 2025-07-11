package com.codingmate.ranking.batch;

import com.codingmate.ranking.dto.RankingReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

@Component
@RequiredArgsConstructor
public class RankRedisWriter implements ItemWriter<RankingReadDto>, StepExecutionListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private PriorityQueue<RankingReadDto> top10;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        top10 = new PriorityQueue<>(Comparator.comparingLong(RankingReadDto::score)); // 오름차순
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        String key = "ranking:daily:" + LocalDate.now(); // 예: ranking:daily:2025-07-11
        List<RankingReadDto> sorted = new ArrayList<>(top10);
        sorted.sort(Comparator.comparingLong(RankingReadDto::score).reversed());

        redisTemplate.opsForValue().set(key, sorted, Duration.ofDays(1));
        return ExitStatus.COMPLETED;
    }

    @Override
    public void write(Chunk<? extends RankingReadDto> chunk) throws Exception {
        for (RankingReadDto user : chunk) {
            top10.offer(user);
            if (top10.size() > 10) {
                top10.poll(); // 가장 낮은 점수 제거
            }
        }
    }
}