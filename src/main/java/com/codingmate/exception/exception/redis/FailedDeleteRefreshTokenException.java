package com.codingmate.exception.exception.redis;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 리프레쉬 토큰을 Redis에서 삭제하지 못해서 발생하는 예외.
 * */
public class FailedDeleteRefreshTokenException extends BusinessException {
    public FailedDeleteRefreshTokenException(ErrorMessage message) {
        super(message);
    }

    public FailedDeleteRefreshTokenException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public FailedDeleteRefreshTokenException(String reason) {
        super(reason);
    }
}