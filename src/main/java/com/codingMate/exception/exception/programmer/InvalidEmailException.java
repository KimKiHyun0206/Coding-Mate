package com.codingMate.exception.exception.programmer;

import com.codingMate.exception.BusinessException;
import com.codingMate.exception.dto.ErrorMessage;

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
