package com.codingmate.exception.exception.jwt;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 지원하지 않는 형식의 토큰일 때 발생하는 예외.
 * */
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