package com.codingmate.ranking.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RankingJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job rankingJob;

    @Scheduled(cron = "0 0 12 * * ?") // 매일 정오
    public void runRankingJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis()) // 중복 실행 방지용
                .toJobParameters();

        jobLauncher.run(rankingJob, params);
    }
}
