package com.codingmate.exception.exception.programmer;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

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
