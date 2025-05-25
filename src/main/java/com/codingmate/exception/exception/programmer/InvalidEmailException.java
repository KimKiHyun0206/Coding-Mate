package com.codingmate.exception.exception.programmer;

import com.codingmate.exception.BusinessException;
import com.codingmate.exception.dto.ErrorMessage;

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
