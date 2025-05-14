package com.codingMate.exception.exception.redis;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class InvalidRefreshTokenException extends BusinessException {
    public InvalidRefreshTokenException(ErrorMessage message) {
        super(message);
    }

    public InvalidRefreshTokenException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public InvalidRefreshTokenException(String reason) {
        super(reason);
    }
}