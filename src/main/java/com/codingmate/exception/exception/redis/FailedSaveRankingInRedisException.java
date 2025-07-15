package com.codingmate.exception.exception.redis;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * Redis에 랭킹을 저장하지 못했을 때 발생하는 예외.
 * <ul>
 *     <li>연결 문제로 저장에 실패할 수 있다.</li>
 *     <li>예기치 못한 문제로 저장에 실패할 수 있다.</li>
 * </ul>
 * */
public class FailedSaveRankingInRedisException extends BusinessException {
    public FailedSaveRankingInRedisException(ErrorMessage message) {
        super(message);
    }

    public FailedSaveRankingInRedisException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public FailedSaveRankingInRedisException(String reason) {
        super(reason);
    }
}