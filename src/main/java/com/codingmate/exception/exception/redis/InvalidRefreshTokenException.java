package com.codingmate.exception.exception.redis;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

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