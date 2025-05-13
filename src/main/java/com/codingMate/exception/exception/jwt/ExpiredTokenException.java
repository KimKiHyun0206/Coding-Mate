package com.codingMate.exception.exception.jwt;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class ExpiredTokenException extends BusinessException {
    public ExpiredTokenException(ErrorMessage message) {
        super(message);
    }

    public ExpiredTokenException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public ExpiredTokenException(String reason) {
        super(reason);
    }
}