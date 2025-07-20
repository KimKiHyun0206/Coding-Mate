package com.codingmate.exception.exception.email;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 이메일 인증에 필요한 이메일이 정규식에 알맞지 않을 경우 예외를 발생시킨다.
 * */
public class IllegalEmailRegexException extends BusinessException {
    public IllegalEmailRegexException(ErrorMessage message) {
        super(message);
    }

    public IllegalEmailRegexException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public IllegalEmailRegexException(String reason) {
        super(reason);
    }
}