package com.codingMate.exception.exception.redis;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class RefreshTokenIsNullException extends BusinessException {
    public RefreshTokenIsNullException(ErrorMessage message) {
        super(message);
    }

    public RefreshTokenIsNullException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public RefreshTokenIsNullException(String reason) {
        super(reason);
    }
}
