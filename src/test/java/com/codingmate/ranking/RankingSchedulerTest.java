package com.codingmate.ranking;

import com.codingmate.ranking.scheduler.RankingJobScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class RankingSchedulerTest {
    @Autowired
    private RankingJobScheduler scheduler;

    @Autowired
    private JobExplorer jobExplorer;

    @Test
    void runRankingJobManually() throws Exception {
        // when
        scheduler.runRankingJob();

        // then (검증: Job 실행 결과가 COMPLETED인지 확인)
        // 최신 JobExecution을 조회해서 상태 확인
        List<JobInstance> instances = jobExplorer.getJobInstances("rankingJob", 0, 1);
        assertFalse(instances.isEmpty());

        JobInstance latestInstance = instances.get(0);
        List<JobExecution> executions = jobExplorer.getJobExecutions(latestInstance);
        JobExecution latestExecution = executions.get(0);

        assertEquals(ExitStatus.COMPLETED, latestExecution.getExitStatus());
    }
}