package com.codingmate.exception.exception.redis;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

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