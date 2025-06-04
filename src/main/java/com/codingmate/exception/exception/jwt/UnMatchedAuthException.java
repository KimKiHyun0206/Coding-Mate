package com.codingmate.exception.exception.jwt;

import com.codingmate.exception.BusinessException;

public class UnMatchedAuthException extends BusinessException {
    public UnMatchedAuthException(String message) {
        super(message);
    }
}