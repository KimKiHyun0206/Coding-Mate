package com.codingmate.ranking.batch;

import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.ranking.RankingCountException;
import com.codingmate.ranking.dto.SolveCountRankingDto;
import com.codingmate.ranking.service.RankKeyGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.redis.core.RedisTemplate;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * {@code SolveCountRankReader}가 읽은 정보를 Redis에 저장하는 Writer
 *
 * @author duskafka
 * */
@Slf4j
@RequiredArgsConstructor
public class RankRedisWriter implements ItemWriter<SolveCountRankingDto>, StepExecutionListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RankKeyGenerator rankKeyGenerator;
    private volatile PriorityQueue<SolveCountRankingDto> top10;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        top10 = new PriorityQueue<>(Comparator.comparingLong(SolveCountRankingDto::score).reversed());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        String key = rankKeyGenerator.getTodayRankingKey(); // 예: ranking:daily:2025-07-11
        List<SolveCountRankingDto> sorted = new ArrayList<>(top10)  ;
        sorted.sort(Comparator.comparingLong(SolveCountRankingDto::score).reversed());

        redisTemplate.opsForValue().set(key, sorted, Duration.ofDays(1));

        log.info("Redis 저장 key={} value={}", key, sorted);

        return ExitStatus.COMPLETED;
    }

    @Override
    public void write(Chunk<? extends SolveCountRankingDto> chunk) throws Exception {
        try {
            synchronized (this) {
                for (SolveCountRankingDto solveCountRankingDto : chunk) {
                    top10.offer(solveCountRankingDto);
                    if (top10.size() > 10) {
                        top10.poll();
                    }
                }
            }
        } catch (Exception e) {
            log.error("랭킹 카운트 중에 에러가 발생했습니다.", e);
            throw new RankingCountException(ErrorMessage.RANKING_COUNT, e.getMessage());
        }
    }
}