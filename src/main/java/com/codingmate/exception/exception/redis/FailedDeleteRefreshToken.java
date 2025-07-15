package com.codingmate.exception.exception.redis;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 리프레쉬 토큰을 Redis에서 삭제하지 못해서 발생하는 예외.
 * */
public class FailedDeleteRefreshToken extends BusinessException {
    public FailedDeleteRefreshToken(ErrorMessage message) {
        super(message);
    }

    public FailedDeleteRefreshToken(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public FailedDeleteRefreshToken(String reason) {
        super(reason);
    }
}