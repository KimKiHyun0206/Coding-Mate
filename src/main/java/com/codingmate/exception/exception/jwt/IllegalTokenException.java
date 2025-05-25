package com.codingmate.exception.exception.jwt;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

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