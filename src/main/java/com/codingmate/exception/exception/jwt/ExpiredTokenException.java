package com.codingmate.exception.exception.jwt;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

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