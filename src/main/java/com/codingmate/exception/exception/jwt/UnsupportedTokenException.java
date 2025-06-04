package com.codingmate.exception.exception.jwt;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

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