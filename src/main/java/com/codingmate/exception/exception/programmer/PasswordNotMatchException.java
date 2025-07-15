package com.codingmate.exception.exception.programmer;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 로그인 요청 히 {@code password}가 일치하지 않아서 발생하는 예외.
 * */
public class PasswordNotMatchException extends BusinessException {
    public PasswordNotMatchException(ErrorMessage message) {
        super(message);
    }

    public PasswordNotMatchException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public PasswordNotMatchException(String reason) {
        super(reason);
    }
}
