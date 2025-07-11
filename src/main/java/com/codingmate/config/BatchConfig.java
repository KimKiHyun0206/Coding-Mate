package com.codingmate.config;

import com.codingmate.ranking.batch.RankRedisWriter;
import com.codingmate.ranking.batch.RankReader;
import com.codingmate.ranking.dto.RankingReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {
    private final RankReader rankReader;
    private final RankRedisWriter rankRedisWriter;


    @Bean
    public Step rankingStep(JobRepository jobRepository, PlatformTransactionManager txManager) {
        return new StepBuilder("rankingStep", jobRepository)
                .<RankingReadDto, RankingReadDto>chunk(10, txManager)
                .reader(rankReader)
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