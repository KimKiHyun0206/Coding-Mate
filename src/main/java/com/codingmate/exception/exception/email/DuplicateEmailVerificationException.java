package com.codingmate.exception.exception.email;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 이미 인증된 이메일 요청이 새로 인증되려고 시도될 때 예외를 발생시킨다.
 * */
public class DuplicateEmailVerificationException extends BusinessException {
    public DuplicateEmailVerificationException(ErrorMessage message) {
        super(message);
    }

    public DuplicateEmailVerificationException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public DuplicateEmailVerificationException(String reason) {
        super(reason);
    }
}