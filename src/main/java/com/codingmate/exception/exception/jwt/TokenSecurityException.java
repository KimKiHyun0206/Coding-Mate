package com.codingmate.exception.exception.jwt;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * JWT Signature가 올바르지 않을 때 발생하는 예외
 *
 * <li>SecurityException가 발생할 때 처리하기 위한 예외이다.</li>
 * */
public class TokenSecurityException extends BusinessException {

    public TokenSecurityException(ErrorMessage message) {
        super(message);
    }

    public TokenSecurityException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public TokenSecurityException(String reason) {
        super(reason);
    }
}