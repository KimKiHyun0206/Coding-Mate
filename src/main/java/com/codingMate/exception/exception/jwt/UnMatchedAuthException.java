package com.codingMate.exception.exception.jwt;

import com.codingMate.exception.BusinessException;

public class UnMatchedAuthException extends BusinessException {
    public UnMatchedAuthException(String message) {
        super(message);
    }
}