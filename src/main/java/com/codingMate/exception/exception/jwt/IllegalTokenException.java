package com.codingMate.exception.exception.jwt;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class IllegalTokenException extends BusinessException {
    public IllegalTokenException(ErrorMessage message) {
        super(message);
    }

    public IllegalTokenException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public IllegalTokenException(String reason) {
        super(reason);
    }
}