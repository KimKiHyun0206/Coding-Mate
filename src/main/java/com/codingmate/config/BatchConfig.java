package com.codingmate.config;

import com.codingmate.ranking.batch.RankRedisWriter;
import com.codingmate.ranking.batch.SolveCountRankReader;
import com.codingmate.ranking.dto.SolveCountRankingDto;
import com.codingmate.ranking.service.RankKeyGenerator;
import com.codingmate.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {
    private final RankingService rankingService;

    @Bean
    @StepScope
    public SolveCountRankReader solveCountRankReader() {
        return new SolveCountRankReader(rankingService);
    }

    @Bean
    @StepScope
    public RankRedisWriter rankRedisWriter(
            RedisTemplate<String, Object> redisTemplate,
            RankKeyGenerator rankKeyGenerator
    ) {
        return new RankRedisWriter(redisTemplate, rankKeyGenerator);
    }

    @Bean
    public Step rankingStep(
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            RankRedisWriter rankRedisWriter,
            SolveCountRankReader solveCountRankReader
    ) {
        return new StepBuilder("rankingStep", jobRepository)
                .<SolveCountRankingDto, SolveCountRankingDto>chunk(10, txManager)
                .reader(solveCountRankReader)
                .writer(rankRedisWriter)
                .build();
    }

    @Bean
    public Job rankingJob(JobRepository jobRepository, Step rankingStep) {
        return new JobBuilder("rankingJob", jobRepository)
                .start(rankingStep)
                .build();
    }
}