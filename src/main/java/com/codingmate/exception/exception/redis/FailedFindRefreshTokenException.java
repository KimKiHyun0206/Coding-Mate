package com.codingmate.exception.exception.redis;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * Redis에서 리프래쉬 토큰을 찾지 못해서 발생하는 예외.
 * */
public class FailedFindRefreshTokenException extends BusinessException {
    public FailedFindRefreshTokenException(ErrorMessage message) {
        super(message);
    }

    public FailedFindRefreshTokenException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public FailedFindRefreshTokenException(String reason) {
        super(reason);
    }
}
