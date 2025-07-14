package com.codingmate.exception.exception.ranking;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 배치 작업이 이미 실행 중일 때 발생하는 예외
 *
 * <p>스프링 배치의 JobExecutionAlreadyRunningException을 비즈니스 예외로 래핑하여 처리합니다.</p>
 * <p>RankingJobScheduler에서 중복 실행을 방지하기 위해 사용된다</p>
 * */
public class JobAlreadyRunningException extends BusinessException {
    public JobAlreadyRunningException(ErrorMessage message) {
        super(message);
    }

    public JobAlreadyRunningException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public JobAlreadyRunningException(String reason) {
        super(reason);
    }
}