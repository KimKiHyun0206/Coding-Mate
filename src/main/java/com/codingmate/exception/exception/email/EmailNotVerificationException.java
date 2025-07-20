package com.codingmate.exception.exception.email;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 회원가입 시 이메일이 인증되지 않았을 때 발생시키는 오류
 * */
public class EmailNotVerificationException extends BusinessException {
    public EmailNotVerificationException(ErrorMessage message) {
        super(message);
    }

    public EmailNotVerificationException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public EmailNotVerificationException(String reason) {
        super(reason);
    }
}