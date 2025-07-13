package com.codingmate.ranking.scheduler;

import com.codingmate.common.annotation.Explanation;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Explanation(
        responsibility = "매일 정오가 되면 랭킹을 갱신하는 스케줄러",
        detail = "매일 정오에 실행되고, 중복 실행을 방지했다.",
        lastReviewed = "2025.07.13"
)
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


