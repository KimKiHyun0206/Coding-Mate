package com.codingmate.exception.exception.redis;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

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
