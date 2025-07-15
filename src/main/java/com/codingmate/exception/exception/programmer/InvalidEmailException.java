package com.codingmate.exception.exception.programmer;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

/**
 * 유효하지 않은 이메일 형식일 때 발생하는 예외.
 * */
public class InvalidEmailException extends BusinessException {
    public InvalidEmailException(ErrorMessage message) {
        super(message);
    }

    public InvalidEmailException(ErrorMessage message, String reason) {
        super(message, reason);
    }

    public InvalidEmailException(String reason) {
        super(reason);
    }
}
