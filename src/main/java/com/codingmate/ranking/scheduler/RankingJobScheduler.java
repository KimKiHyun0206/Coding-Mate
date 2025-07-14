package com.codingmate.ranking.scheduler;

import com.codingmate.common.annotation.Explanation;
import com.codingmate.exception.dto.ErrorMessage;
import com.codingmate.exception.exception.ranking.JobAlreadyExecutionException;
import com.codingmate.exception.exception.ranking.JobBuilderBuildInvalidParametersException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
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

        try {
            jobLauncher.run(rankingJob, params);
            log.info("Ranking job이 성공적으로 실행됐습니다 {}", System.currentTimeMillis());
        } catch (JobExecutionAlreadyRunningException jobExecutionAlreadyRunningException) {
            log.error("동일한 Job 인스턴스가 이미 실행 중인데, 또 다시 실행을 시도했습니다.");
            throw new JobAlreadyExecutionException(ErrorMessage.JOB_ALREADY_EXECUTION, jobExecutionAlreadyRunningException.getMessage());
        } catch (JobParametersInvalidException jobParametersInvalidException) {
            log.error("JobParameterBuilder를 통해생성된 JobParameters가 유효하지 않습니다.");
            throw new JobBuilderBuildInvalidParametersException(ErrorMessage.JOB_BUILDER_BUILD_INVALID_PARAMETERS, jobParametersInvalidException.getMessage());
        }
    }
}


