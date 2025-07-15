package com.codingmate.exception.exception.programmer;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 유효하지 않은 비밀번호 형식일 때 발생하는 예외.
 * */
public class InvalidPasswordException extends BusinessException {
    public InvalidPasswordException(ErrorMessage message) {
        super(message);
    }

    public InvalidPasswordException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public InvalidPasswordException(String reason) {
        super(reason);
    }
}
