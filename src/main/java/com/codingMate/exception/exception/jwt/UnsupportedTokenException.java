package com.codingMate.exception.exception.jwt;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

public class UnsupportedTokenException extends BusinessException {
    public UnsupportedTokenException(ErrorMessage message) {
        super(message);
    }

    public UnsupportedTokenException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public UnsupportedTokenException(String reason) {
        super(reason);
    }
}