package com.codingmate.exception.exception.redis;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

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