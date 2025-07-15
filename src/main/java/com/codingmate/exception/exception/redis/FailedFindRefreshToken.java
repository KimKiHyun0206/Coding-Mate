package com.codingmate.exception.exception.redis;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * Redis에서 리프래쉬 토큰을 찾지 못해서 발생하는 예외.
 * */
public class FailedFindRefreshToken extends BusinessException {
    public FailedFindRefreshToken(ErrorMessage message) {
        super(message);
    }

    public FailedFindRefreshToken(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public FailedFindRefreshToken(String reason) {
        super(reason);
    }
}
